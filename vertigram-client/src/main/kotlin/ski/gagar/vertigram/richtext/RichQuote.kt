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
    property = "quoteParseMode",
    defaultImpl = QuoteWithEntities::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MarkdownV1Quote::class, name = ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownQuote::class, name = ParseMode.MARKDOWN_V2_STR),
    JsonSubTypes.Type(value = HtmlQuote::class, name = ParseMode.HTML_STR),
)
sealed interface RichQuote {
    val quote: String
    @get:JsonIgnore
    val quoteParseMode: ParseMode?
        get() = null
    @get:JsonIgnore
    val quoteEntities: List<MessageEntity>?
        get() = null
    companion object {
        operator fun invoke(quote: String,
                            quoteParseMode: ParseMode?,
                            quoteEntities: List<MessageEntity>?) : RichQuote {
            if (quoteParseMode != null) require(null == quoteEntities)
            return when (quoteParseMode) {
                ParseMode.MARKDOWN -> MarkdownV1Quote(quote)
                ParseMode.MARKDOWN_V2 -> MarkdownQuote(quote)
                ParseMode.HTML -> HtmlQuote(quote)
                else -> QuoteWithEntities(quote, quoteEntities ?: listOf())
            }
        }
    }
}

@Suppress("DEPRECATION")
@Deprecated("Consider using other mode", replaceWith = ReplaceWith("MarkdownQuote"))
data class MarkdownV1Quote(
    override val quote: String
) : RichQuote {
    override val quoteParseMode: ParseMode = ParseMode.MARKDOWN
}

data class MarkdownQuote(
    override val quote: String
) : RichQuote {
    override val quoteParseMode: ParseMode = ParseMode.MARKDOWN_V2
}

data class HtmlQuote(
    override val quote: String
) : RichQuote {
    override val quoteParseMode: ParseMode = ParseMode.HTML
}


data class QuoteWithEntities(
    override val quote: String,
    override val quoteEntities: List<MessageEntity> = listOf(),
) : RichQuote

