package ski.gagar.vertigram.entities

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo


// Type filed here is quite fine with Jackson polymorphism.
// However for other cases we want to totally disable type info when sending a message to Telegram
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes(
    JsonSubTypes.Type(value = InlineQueryResult::class)
)
sealed class InlineQueryResult

data class InlineQueryResultArticle(
    val id: String,
    val title: String,
    val inputMessageContent: InputMessageContent,
    // TODO replyMarkup
    val url: String? = null,
    val hideUrl: Boolean = false, // Optional by specification, sent as false by default
    val description: String? = null,
    val thumbUrl: String? = null,
    val thumbWidth: Long? = null,
    val thumbHeight: Long? = null
) : InlineQueryResult() {
    val type = "article"
}