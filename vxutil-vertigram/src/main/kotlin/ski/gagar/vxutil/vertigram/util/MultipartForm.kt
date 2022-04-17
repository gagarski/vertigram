package ski.gagar.vxutil.vertigram.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializer
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import com.fasterxml.jackson.databind.util.TokenBuffer
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.uncheckedCast
import ski.gagar.vxutil.uncheckedCastOrNull
import ski.gagar.vxutil.vertigram.methods.SendMediaGroup
import ski.gagar.vxutil.vertigram.types.InputMedia
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.vertigram.types.attachments.attachIndirectly
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue

class ObjectMapperWithMultipart(delegate: ObjectMapper) : ObjectMapper(delegate) {

    fun JsonParser.getDeserializer(deserConfig: DeserializationConfig, type: JavaType): JsonDeserializer<Any> {
        val ctxt: DeserializationContext = createDeserializationContext(this, deserConfig)
        return _findRootDeserializer(ctxt, type)

    }
    fun JsonParser.toMap(): Map<String, Any?>? {
        val deserConfig: DeserializationConfig = getDeserializationConfig()
        val mapType = typeFactory.constructType(Map::class.java)
        val t: JsonToken = _initForReading(this, mapType)
        val map: Map<String, Any?>? = when (t) {
            JsonToken.VALUE_NULL -> {
                val ctxt: DeserializationContext = createDeserializationContext(this, deserConfig)
                _findRootDeserializer(ctxt, mapType).getNullValue(ctxt).uncheckedCastOrNull()
            }
            JsonToken.END_ARRAY, JsonToken.END_OBJECT -> {
                null
            }
            else -> { // pointing to event other than null
                val ctxt: DeserializationContext = createDeserializationContext(this, deserConfig)
                val deser: JsonDeserializer<Any> = _findRootDeserializer(ctxt, mapType)
                // note: no handling of unwrapping
                deser.deserialize(this, ctxt).uncheckedCastOrNull()
            }
        }
        this.close()
        return map
    }

    fun toMultipart(obj: Any): MultipartForm = MultipartForm.create().apply {
        val buf = TokenBuffer(this@ObjectMapperWithMultipart, false)
        val prov = _serializerProvider(serializationConfig)
        val ser = prov
            .uncheckedCast<DefaultSerializerProvider>()
            .findTypedValueSerializer(obj.javaClass, true, null)

        if (ser !is BeanSerializer) {
            throw IllegalArgumentException("Only types with BeanSerializer are supported to be serialized to multipart")
        }

        val boostedSer = BeanSerializerWithMultipart(ser)

        val deferredMedia = boostedSer.serializeToMultipart(obj, buf, prov)

        val map = buf.asParser().toMap()

        for ((k, v) in map ?: return@apply) {
            when (v) {
                null -> continue
                is Boolean -> attributeIfTrue(k, v)
                is Map<*, *> -> attributeIfNotNull(k, writeValueAsString(v))
                is List<*> -> attributeIfNotNull(k, writeValueAsString(v))
                else -> attributeIfNotNull(k, v.toString())
            }
        }

        for ((name, attachment) in deferredMedia) {
            if (attachment.isIndirect) {
                attachIndirectly(name, attachment.attachment)
            } else {
                attachDirectly(name, attachment.attachment)
            }
        }
    }
}
class BeanSerializerWithMultipart(delegate: BeanSerializer) : BeanSerializer(delegate) {
    fun serializeToMultipart(bean: Any, gen: JsonGenerator, provider: SerializerProvider): Map<String, AttachmentInfo> {
        if (_objectIdWriter != null) {
            throw IllegalStateException("_objectIdWriter is not supported")
        }
        if (_propertyFilterId != null) {
            throw IllegalStateException("_propertyFilterId is not supported")
        } else {
            gen.writeStartObject()
            val deferredMedia = serializeFieldsMultipart(bean, gen, provider)
            gen.writeEndObject()
            return deferredMedia
        }
    }
    fun serializeFieldsMultipart(bean: Any, gen: JsonGenerator, provider: SerializerProvider): Map<String, AttachmentInfo> {
        val props: Array<BeanPropertyWriter?> = if (_filteredProps != null && provider.activeView != null) {
            _filteredProps
        } else {
            _props
        }
        var i = 0
        try {
            val deferredMedia = mutableMapOf<String, AttachmentInfo>()
            for (prop in props) {
                prop?.let {
                    if (null != prop.getAnnotation(TgMedia::class.java)) {
                        deferredMedia.putAll(MediaInstantiatingBeanPropertyWriter(prop).serializeMedia(bean, gen, provider))
                    } else {
                        prop.serializeAsField(bean, gen, provider)
                    }
                }
                ++i
            }
            // @JsonAnyGetter is not supported
            //  _anyGetterWriter?.getAndSerialize(bean, gen, provider)
            return deferredMedia
        } catch (e: Exception) {
            val name = if (i == props.size) "[anySetter]" else props[i]?.name ?: "[null]"
            wrapAndThrow(provider, e, bean, name)
            throw AssertionError("Should not happen")
        } catch (e: StackOverflowError) {
            val mapE = JsonMappingException(gen, "Infinite recursion (StackOverflowError)", e)
            val name = if (i == props.size) "[anySetter]" else props[i]?.name ?: "[null]"
            mapE.prependPath(JsonMappingException.Reference(bean, name))
            throw mapE
        }
    }

}


class MediaInstantiatingBeanPropertyWriter(delegate: BeanPropertyWriter) : BeanPropertyWriter(delegate) {
    private fun processSingleMedia(
        value: InputMedia,
        deferred: MutableMap<String, AttachmentInfo>,
        index: Int? = null
    ): InputMedia {
        val indexStr = index?.let {
            "_$it"
        }

        val mediaName = "${ATTACHMENT}_${_name}${indexStr}_${MEDIA}"
        val thumbName = "${ATTACHMENT}_${_name}${indexStr}_${THUMB}"

        val processedMedia = value.instantiate(
            media = value.media.getIndirectAttachment(mediaName),
            thumb = value.thumb?.getIndirectAttachment(thumbName)
        )

        deferred[mediaName] = AttachmentInfo(value.media, true)
        value.thumb?.let {
            deferred[thumbName] = AttachmentInfo(it, true)
        }

        return processedMedia
    }

    fun serializeMedia(
        bean: Any, gen: JsonGenerator,
        prov: SerializerProvider?
    ): Map<String, AttachmentInfo> {
        // inlined 'get()'
        val value = if (_accessorMethod == null) _field[bean] else _accessorMethod.invoke(bean, *arrayOf<Any?>())

        val deferredAttachments = mutableMapOf<String, AttachmentInfo>()
        val processedValue = when (value) {
            is Attachment -> {
                deferredAttachments[name] = AttachmentInfo(value, false)
                return deferredAttachments // no need to write to json
            }
            is InputMedia -> {
                processSingleMedia(value, deferredAttachments)
            }
            is Collection<*> -> {
                value.withIndex().map { (index, it) ->
                    if (it is InputMedia) {
                        processSingleMedia(it, deferredAttachments, index)
                    } else {
                        it
                    }
                }.toList()
            }
            else -> {
                value
            }
        }

        if (processedValue == null) {
            if (_nullSerializer != null) {
                gen.writeFieldName(_name)
                _nullSerializer.serialize(null, gen, prov)
            }
            return deferredAttachments
        }
        var ser = _serializer
        if (ser == null) {
            val cls: Class<*> = processedValue.javaClass
            val m = _dynamicSerializers
            ser = m.serializerFor(cls)
            if (ser == null) {
                ser = _findAndAddDynamic(m, cls, prov)
            }
        }
        if (_suppressableValue != null) {
            if (MARKER_FOR_EMPTY === _suppressableValue) {
                if (ser!!.isEmpty(prov, processedValue)) {
                    return deferredAttachments
                }
            } else if (_suppressableValue == processedValue) {
                return deferredAttachments
            }
        }
        if (processedValue === bean) {
            // four choices: exception; handled by call; pass-through or write null
            if (_handleSelfReference(bean, gen, prov, ser)) {
                return deferredAttachments
            }
        }
        gen.writeFieldName(_name)
        if (_typeSerializer == null) {
            ser!!.serialize(processedValue, gen, prov)
        } else {
            ser!!.serializeWithType(processedValue, gen, prov, _typeSerializer)
        }
        return deferredAttachments
    }

    companion object {
        const val ATTACHMENT = "attachment"
        const val MEDIA = "media"
        const val THUMB = "thumb"
    }
}

data class AttachmentInfo(val attachment: Attachment, val isIndirect: Boolean)

annotation class TgMedia


fun telegramJsonMapperWithMultipart() =
    ObjectMapperWithMultipart(telegramJsonMapper())

val TELEGRAM_JSON_MAPPER_WITH_MULTIPART = telegramJsonMapperWithMultipart()
