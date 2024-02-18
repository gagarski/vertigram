package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.types.attachments.StringAttachment

internal class AttachmentDeserializer : JsonDeserializer<Attachment>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Attachment =
        when (parser.currentToken) {
            JsonToken.VALUE_STRING -> StringAttachment(parser.valueAsString)
            else -> {
                throw JsonMappingException.from(parser, "attachment should be string")
            }
        }

    override fun handledType(): Class<Attachment> = Attachment::class.java
}
