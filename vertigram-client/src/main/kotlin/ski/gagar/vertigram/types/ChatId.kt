package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.util.json.TelegramIgnoreTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = LongChatId::class),
    JsonSubTypes.Type(value = StringChatId::class),
)
@TelegramIgnoreTypeInfo
sealed interface ChatId {
    companion object {
        fun string(string: String) = StringChatId(string)
        fun long(long: Long) = LongChatId(long)
        fun int(int: Int) = LongChatId(int.toLong())
    }
}

data class LongChatId(val long: Long) : ChatId {
    override fun toString(): String = long.toString()
}

data class StringChatId(val string: String) : ChatId {
    override fun toString(): String = string
}


fun String.toChatId() = StringChatId(this)
fun Long.toChatId() = LongChatId(this)
fun Int.toChatId() = LongChatId(this.toLong())
