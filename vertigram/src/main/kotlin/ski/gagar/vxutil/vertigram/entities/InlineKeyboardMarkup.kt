package ski.gagar.vxutil.vertigram.entities

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = InlineKeyboardMarkup::class),
    JsonSubTypes.Type(value = ReplyKeyboardMarkup::class),
    JsonSubTypes.Type(value = ReplyKeyboardRemove::class),
    JsonSubTypes.Type(value = ForceReply::class)
)
sealed class ReplyMarkup


data class InlineKeyboardMarkup(
    val inlineKeyboard: List<List<InlineKeyboardButton>>
) : ReplyMarkup()


data class ReplyKeyboardMarkup(
    val keyboard: List<List<InlineKeyboardButton>>,
    val resizeKeyboard: Boolean = false,
    val oneTimeKeyboard: Boolean = false,
    val selective: Boolean = false
) : ReplyMarkup()

data class ReplyKeyboardRemove(
    val selective: Boolean = false
) : ReplyMarkup() {
    val removeKeyboard: Boolean = true
}

data class ForceReply(
    val selective: Boolean = false
) : ReplyMarkup() {
    val forceReply: Boolean = true
}
