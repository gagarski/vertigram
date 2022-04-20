package ski.gagar.vxutil.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import ski.gagar.vxutil.vertigram.types.attachments.UrlAttachment

internal class UrlAttachmentSerializer : JsonSerializer<UrlAttachment>() {
    override fun serialize(value: UrlAttachment, gen: JsonGenerator, serializers: SerializerProvider) =
        gen.writeString(value.url)

    override fun serializeWithType(
        value: UrlAttachment,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) = serialize(value, gen, serializers)

    override fun handledType(): Class<UrlAttachment> = UrlAttachment::class.java
}
