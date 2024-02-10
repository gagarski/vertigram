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
    property = "parseMode",
    defaultImpl = TextWithEntities::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MarkdownV1Text::class, name = ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownText::class, name = ParseMode.MARKDOWN_V2_STR),
    JsonSubTypes.Type(value = HtmlText::class, name = ParseMode.HTML_STR),
)
sealed interface RichText {
    val text: String
    @get:JsonIgnore
    val parseMode: ParseMode?
        get() = null
    @get:JsonIgnore
    val entities: List<MessageEntity>?
        get() = null

    companion object {
        operator fun invoke(text: String,
                            parseMode: ParseMode?,
                            entities: List<MessageEntity>?) : RichText {
            if (parseMode != null) require(null == entities)
            return when (parseMode) {
                ParseMode.MARKDOWN -> MarkdownV1Text(text)
                ParseMode.MARKDOWN_V2 -> MarkdownText(text)
                ParseMode.HTML -> HtmlText(text)
                else -> TextWithEntities(text, entities ?: listOf())
            }
        }
    }
}

@Suppress("DEPRECATION")
@Deprecated("Consider using other mode", replaceWith = ReplaceWith("MarkdownV2Text"))
data class MarkdownV1Text(
    override val text: String
) : RichText {
    override val parseMode: ParseMode = ParseMode.MARKDOWN
}

data class MarkdownText(
    override val text: String
) : RichText {
    override val parseMode: ParseMode = ParseMode.MARKDOWN_V2
}

data class HtmlText(
    override val text: String
) : RichText {
    override val parseMode: ParseMode = ParseMode.HTML
}


data class TextWithEntities(
    override val text: String,
    override val entities: List<MessageEntity> = listOf(),
) : RichText

