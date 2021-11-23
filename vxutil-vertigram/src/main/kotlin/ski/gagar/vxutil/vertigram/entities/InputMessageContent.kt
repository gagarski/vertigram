package ski.gagar.vxutil.vertigram.entities

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = InputTextMessageContent::class)
)
sealed class InputMessageContent

data class InputTextMessageContent(
    val messageText: String,
    val parseMode: ParseMode? = null
) : InputMessageContent()
