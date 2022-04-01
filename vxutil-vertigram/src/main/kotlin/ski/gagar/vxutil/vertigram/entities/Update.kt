package ski.gagar.vxutil.vertigram.entities

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = ParsedUpdate::class),
    JsonSubTypes.Type(value = MalformedUpdate::class)
)
sealed interface Update {
    val updateId: Long
}

data class ParsedUpdate(
    override val updateId: Long,
    val message: Message? = null,
    val editedMessage: Message? = null,
    val channelPost: Message? = null,
    val editedChannelPost: Message? = null,
    val inlineQuery: InlineQuery? = null,
    val chosenInlineResult: ChosenInlineResult? = null,
    val callbackQuery: CallbackQuery? = null,
    val shippingQuery: ShippingQuery? = null,
    val preCheckoutQuery: PreCheckoutQuery? = null,
    val poll: Poll? = null
) : Update

data class MalformedUpdate(
    override val updateId: Long
) : Update

class UpdateList(private val delegate: List<Update>) : List<Update> by delegate
class ParsedUpdateList(private val delegate: List<ParsedUpdate>) : List<ParsedUpdate> by delegate