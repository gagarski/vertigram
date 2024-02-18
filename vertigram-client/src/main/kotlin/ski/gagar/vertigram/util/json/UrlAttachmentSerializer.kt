package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import ski.gagar.vertigram.types.attachments.StringAttachment

internal class UrlAttachmentSerializer : JsonSerializer<StringAttachment>() {
    override fun serialize(value: StringAttachment, gen: JsonGenerator, serializers: SerializerProvider) =
        gen.writeString(value.url)

    override fun serializeWithType(
        value: StringAttachment,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) = serialize(value, gen, serializers)

    override fun handledType(): Class<StringAttachment> = StringAttachment::class.java
}
