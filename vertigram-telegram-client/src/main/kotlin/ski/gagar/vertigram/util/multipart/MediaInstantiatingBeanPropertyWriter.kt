package ski.gagar.vertigram.util.multipart

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import ski.gagar.vertigram.types.InputMedia
import ski.gagar.vertigram.types.attachments.Attachment

/**
 * [BeanPropertyWriter] sub-class for [ObjectMapperWithMultipart]
 */
internal class MediaInstantiatingBeanPropertyWriter(delegate: BeanPropertyWriter) : BeanPropertyWriter(delegate) {
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
            media = value.media.getReference(mediaName),
            thumbnail = value.thumbnail?.getReference(thumbName)
        )

        deferred[mediaName] = AttachmentInfo(value.media, true)
        value.thumbnail?.let {
            deferred[thumbName] = AttachmentInfo(it, true)
        }

        return processedMedia
    }

    private fun processSingleSticker(
        value: InputMedia.Sticker,
        deferred: MutableMap<String, AttachmentInfo>,
        index: Int? = null
    ): InputMedia.Sticker {
        val indexStr = index?.let {
            "_$it"
        }
        val stickerName = "${ATTACHMENT}_${_name}${indexStr}_${STICKER}"

        val processedSticker = value.instantiate(
            sticker = value.sticker.getReference(stickerName)
        )

        deferred[stickerName] = AttachmentInfo(value.sticker, true)

        return processedSticker
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
            is InputMedia.Sticker -> {
                processSingleSticker(value, deferredAttachments)
            }
            is Collection<*> -> {
                value.withIndex().map { (index, it) ->
                    when (it) {
                        is InputMedia -> processSingleMedia(it, deferredAttachments, index)
                        is InputMedia.Sticker -> processSingleSticker(it, deferredAttachments, index)
                        else -> it
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
        const val STICKER = "sticker"
    }
}
