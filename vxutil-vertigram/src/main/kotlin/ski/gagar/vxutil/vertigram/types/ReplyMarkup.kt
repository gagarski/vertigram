package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * A common type for Telegram types InlineKeyboardMarkup, ReplyKeyboardMarkup, ReplyKeyboardRemove and ForceReply.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = InlineKeyboardMarkup::class),
    JsonSubTypes.Type(value = ReplyKeyboardMarkup::class),
    JsonSubTypes.Type(value = ReplyKeyboardRemove::class),
    JsonSubTypes.Type(value = ForceReply::class)
)
sealed class ReplyMarkup
