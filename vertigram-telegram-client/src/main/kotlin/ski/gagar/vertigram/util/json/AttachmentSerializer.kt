package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import ski.gagar.vertigram.telegram.types.attachments.Attachment

/**
 * Dummy serializer for [Attachment] to detect bugs with [Attachment] instantiation
 * in [ski.gagar.vertigram.util.multipart.ObjectMapperWithMultipart], used in [TELEGRAM_JSON_MAPPER].
 */
internal class AttachmentSerializer : JsonSerializer<Attachment>() {
    override fun serialize(value: Attachment, gen: JsonGenerator, serializers: SerializerProvider) =
        throw JsonMappingException.from(gen, "Attachment is not serializable, please serialize UrlAttachment")

    override fun handledType(): Class<Attachment> = Attachment::class.java
}
