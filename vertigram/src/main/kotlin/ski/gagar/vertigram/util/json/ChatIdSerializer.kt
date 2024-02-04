package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.LongChatId
import ski.gagar.vertigram.types.StringChatId

internal class ChatIdSerializer : JsonSerializer<ChatId>() {
    override fun serialize(value: ChatId, gen: JsonGenerator, serializers: SerializerProvider) = when(value) {
        is LongChatId -> gen.writeNumber(value.long)
        is StringChatId -> gen.writeString(value.string)
    }

    override fun serializeWithType(
        value: ChatId,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) {
        serialize(value, gen, serializers)
    }

    override fun handledType(): Class<ChatId> = ChatId::class.java
}
