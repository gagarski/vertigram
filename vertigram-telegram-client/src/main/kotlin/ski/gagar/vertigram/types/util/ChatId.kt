package ski.gagar.vertigram.types.util

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * A wrapper for chat id fields having type "Integer or String"
 */
@JsonDeserialize(using = ChatIdDeserializer::class)
sealed interface ChatId {
    companion object {
        /**
         * Create [StringChatId] from [String]
         * @param string string chat id
         */
        fun string(string: String): ChatId = StringChatId(string)

        /**
         * Create [LongChatId] from [Long]
         * @param long long chat id
         *
         */
        fun long(long: Long): ChatId = LongChatId(long)

        /**
         * Create [LongChatId] from [Int]
         * @param int long chat id as int
         */
        fun int(int: Int): ChatId = LongChatId(int.toLong())
    }
}

/**
 * Long chat id.
 */
@JvmInline
value class LongChatId(
    /**
     * chat id itself
     */
    @JsonValue
    val long: Long
) : ChatId

/**
 * String chat id.
 */
@JvmInline
value class StringChatId(
    /**
     * chat id itself
     */
    @JsonValue
    val string: String
) : ChatId


/**
 * Convert [String] to [ChatId]
 *
 * @receiver [String] to convert
 */
fun String.toChatId() = StringChatId(this)

/**
 * Convert [Long] to [ChatId]
 *
 * @receiver [Long] to convert
 */
fun Long.toChatId() = LongChatId(this)

/**
 * Convert [Int] to [ChatId]
 *
 * @receiver [Int] to convert
 */
fun Int.toChatId() = LongChatId(this.toLong())

/**
 * Deserializer for [ChatId].
 *
 * Converts number-shaped JSON value to [LongChatId] and string-shaped one to [StringChatId].
 */
class ChatIdDeserializer : JsonDeserializer<ChatId>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): ChatId = when (parser.currentToken) {
        JsonToken.VALUE_NUMBER_INT -> LongChatId(parser.longValue)
        JsonToken.VALUE_STRING -> StringChatId(parser.valueAsString)

        else -> {
            throw JsonMappingException.from(parser, "chatId should be either integer or string")
        }
    }

    override fun handledType(): Class<ChatId> = ChatId::class.java
}
