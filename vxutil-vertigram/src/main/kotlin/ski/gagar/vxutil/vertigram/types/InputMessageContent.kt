package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vxutil.vertigram.util.json.TgIgnoreTypeInfo

@TgIgnoreTypeInfo
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = InputTextMessageContent::class),
    JsonSubTypes.Type(value = InputLocationMessageContent::class),
    JsonSubTypes.Type(value = InputVenueMessageContent::class),
    JsonSubTypes.Type(value = InputContactMessageContent::class),
    JsonSubTypes.Type(value = InputInvoiceMessageContent::class)
)
sealed interface InputMessageContent
