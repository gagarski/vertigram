package ski.gagar.vertigram.util.multipart

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.BeanSerializer
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import com.fasterxml.jackson.databind.util.TokenBuffer
import io.vertx.core.Vertx
import ski.gagar.vertigram.util.internal.uncheckedCast
import ski.gagar.vertigram.util.internal.uncheckedCastOrNull
import ski.gagar.vertigram.web.multipart.FieldPart
import ski.gagar.vertigram.web.multipart.MultipartForm

/**
 * An ugly attempt to make ObjectMapper support Telegram flavor of multipart form format.
 *
 * Multipart format is only applied on the top level, nested entities are serialized to JSON and inserted
 * to the form as a form field.
 *
 * This class intends to support [ski.gagar.vertigram.telegram.types.InputMedia] fields (both single and lists) and
 * [ski.gagar.vertigram.telegram.types.attachments.Attachment] fields to attach files. Such fields should be annotated
 * with [ski.gagar.vertigram.annotations.TelegramMedia] annotation to be properly handled.
 *
 * This mapper is built on top of regular [ObjectMapper] and should be working "almost as JSON" on multipart part
 * of serialization. However, there are some known limitations:
 *  - [com.fasterxml.jackson.annotation.JsonAnyGetter] is not supported
 *  - [com.fasterxml.jackson.annotation.JsonFilter] is not supported
 *  - Top level objects which are not using [BeanSerializer] are not supported
 */
internal class ObjectMapperWithMultipart(delegate: ObjectMapper, private val vertx: Vertx) : ObjectMapper(delegate) {
    private fun JsonParser.toMap(): Map<String, Any?>? {
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

    fun toMultipart(obj: Any): MultipartForm = MultipartForm(buildList {
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

        for ((k, v) in map ?: return@buildList) {
            when (v) {
                null -> continue
                is Boolean -> if (v) add(FieldPart(k, v))
                is Map<*, *>, is List<*> -> add(FieldPart(k, writeValueAsString(v)))
                else -> add(FieldPart(k, v))
            }
        }

        for ((name, attachment) in deferredMedia) {
            if (attachment.isIndirect) {
                attachment.attachment.getReferredPart(name, vertx)?.let {
                    add(it)
                }
            } else {
                add(attachment.attachment.getPart(name, vertx))
            }
        }
    })
}

