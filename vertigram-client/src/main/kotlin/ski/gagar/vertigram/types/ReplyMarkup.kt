package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.util.json.TgIgnoreTypeInfo

@TgIgnoreTypeInfo
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = InlineKeyboardMarkup::class),
    JsonSubTypes.Type(value = ReplyKeyboardMarkup::class),
    JsonSubTypes.Type(value = ReplyKeyboardRemove::class),
    JsonSubTypes.Type(value = ForceReply::class)
)
sealed interface ReplyMarkup
