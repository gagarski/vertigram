package ski.gagar.vertigram.richtext

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode

@Suppress("DEPRECATION")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "explanationParseMode",
    defaultImpl = ExplanationWithEntities::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MarkdownV1Explanation::class, name = ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownExplanation::class, name = ParseMode.MARKDOWN_V2_STR),
    JsonSubTypes.Type(value = HtmlExplanation::class, name = ParseMode.HTML_STR),
)
sealed interface RichExplanation {
    val explanation: String
    @get:JsonIgnore
    val explanationParseMode: ParseMode?
        get() = null
    @get:JsonIgnore
    val explanationEntities: List<MessageEntity>?
        get() = null
    companion object {
        operator fun invoke(explanation: String,
                            explanationParseMode: ParseMode?,
                            explanationEntities: List<MessageEntity>?) : RichExplanation {
            if (explanationParseMode != null) require(null == explanationEntities)
            return when (explanationParseMode) {
                ParseMode.MARKDOWN -> MarkdownV1Explanation(explanation)
                ParseMode.MARKDOWN_V2 -> MarkdownExplanation(explanation)
                ParseMode.HTML -> HtmlExplanation(explanation)
                else -> ExplanationWithEntities(explanation, explanationEntities ?: listOf())
            }
        }
    }
}

@Suppress("DEPRECATION")
@Deprecated("Consider using other mode", replaceWith = ReplaceWith("MarkdownExplanation"))
data class MarkdownV1Explanation(
    override val explanation: String
) : RichExplanation {
    override val explanationParseMode: ParseMode = ParseMode.MARKDOWN
}

data class MarkdownExplanation(
    override val explanation: String
) : RichExplanation {
    override val explanationParseMode: ParseMode = ParseMode.MARKDOWN_V2
}

data class HtmlExplanation(
    override val explanation: String
) : RichExplanation {
    override val explanationParseMode: ParseMode = ParseMode.HTML
}


data class ExplanationWithEntities(
    override val explanation: String,
    override val explanationEntities: List<MessageEntity> = listOf(),
) : RichExplanation

