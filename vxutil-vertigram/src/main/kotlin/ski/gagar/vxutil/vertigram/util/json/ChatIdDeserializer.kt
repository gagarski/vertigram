package ski.gagar.vxutil.vertigram.util.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.LongChatId
import ski.gagar.vxutil.vertigram.types.StringChatId

internal class ChatIdDeserializer : JsonDeserializer<ChatId>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): ChatId = when (parser.currentToken) {
        JsonToken.VALUE_NUMBER_INT -> LongChatId(parser.longValue)
        JsonToken.VALUE_STRING -> StringChatId(parser.valueAsString)

        else -> {
            throw JsonMappingException.from(parser, "chatId should be either integer or string")
        }
    }

    override fun handledType(): Class<ChatId> = ChatId::class.java
}
