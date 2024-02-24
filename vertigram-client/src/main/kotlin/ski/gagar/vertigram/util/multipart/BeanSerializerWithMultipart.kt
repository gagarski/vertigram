package ski.gagar.vertigram.util.multipart

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializer
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.types.attachments.Attachment

/**
 * Extends [BeanSerializer] to support multipart for [ski.gagar.vertigram.client.DirectTelegram] purposes.
 *
 * @see serializeToMultipart
 */
internal class BeanSerializerWithMultipart(delegate: BeanSerializer) : BeanSerializer(delegate) {
    /**
     * Resolves all [Attachment] in the [bean]. Serialization results is written to [gen]
     * (which is treated as map on upper level).
     *
     * @return deferred attachments
     */
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
    private fun serializeFieldsMultipart(bean: Any, gen: JsonGenerator, provider: SerializerProvider): Map<String, AttachmentInfo> {
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
                    if (null != prop.getAnnotation(TelegramMedia::class.java)) {
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

internal data class AttachmentInfo(val attachment: Attachment, val isIndirect: Boolean)
