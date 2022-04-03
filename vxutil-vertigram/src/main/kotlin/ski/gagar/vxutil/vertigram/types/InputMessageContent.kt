package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Telegram type InputMessageContent.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = InputTextMessageContent::class),
    JsonSubTypes.Type(value = InputLocationMessageContent::class),
    JsonSubTypes.Type(value = InputVenueMessageContent::class),
    JsonSubTypes.Type(value = InputContactMessageContent::class),
    JsonSubTypes.Type(value = InputInvoiceMessageContent::class)
)
sealed class InputMessageContent

/**
 * Telegram type InputTextMessageContent.
 */
data class InputTextMessageContent(
    val messageText: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val disableWebPagePreview: Boolean = false
) : InputMessageContent()

val InputTextMessageContent.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = entities?.map { InstantiatedEntity(it, this.messageText) } ?: listOf()
