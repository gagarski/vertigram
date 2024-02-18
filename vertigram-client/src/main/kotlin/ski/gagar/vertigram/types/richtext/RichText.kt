package ski.gagar.vertigram.types.richtext

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode

/**
 * A convenience type which replaces triples like `caption`/`captionEntities`/`parseMode` whith a type-safe wrapper.
 *
 * Consider using builders from [ski.gagar.vertigram.markup] package to create the instances:
 *  - [ski.gagar.vertigram.markup.textMarkdown] for [MarkdownV2Text]
 *  - [ski.gagar.vertigram.markup.textHtml] for [HtmlText]
 *  - [ski.gagar.vertigram.markup.textWithEntities] for [TextWithEntities]
 *
 *  There is currently no builder for [MarkdownText] because it's a deprecated format, however it's still supported.
 */
@Suppress("DEPRECATION")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "parseMode",
    defaultImpl = TextWithEntities::class,
    include = JsonTypeInfo.As.EXISTING_PROPERTY
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MarkdownText::class, name = ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownV2Text::class, name = ParseMode.MARKDOWN_V2_STR),
    JsonSubTypes.Type(value = HtmlText::class, name = ParseMode.HTML_STR),
    JsonSubTypes.Type(value = TextWithEntities::class, name = ParseMode.HTML_STR),
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
                ParseMode.MARKDOWN -> MarkdownText(text)
                ParseMode.MARKDOWN_V2 -> MarkdownV2Text(text)
                ParseMode.HTML -> HtmlText(text)
                else -> TextWithEntities(text, entities ?: listOf())
            }
        }
    }
}

/**
 * Deprecated Markdown text, represents the case when `parseMode` is `Markdown`.
 */
@Suppress("DEPRECATION")
@Deprecated("Consider using other mode", replaceWith = ReplaceWith("MarkdownV2Text"))
data class MarkdownText(
    override val text: String
) : RichText {
    override val parseMode: ParseMode = ParseMode.MARKDOWN
}

/**
 * Markdown v. 2 text, represents the case when `parseMode` is `MarkdownV2`.
 */
data class MarkdownV2Text(
    override val text: String
) : RichText {
    override val parseMode: ParseMode = ParseMode.MARKDOWN_V2
}

/**
 * HTML text, represents the case when `parseMode` is `HTML`.
 */
data class HtmlText(
    override val text: String
) : RichText {
    override val parseMode: ParseMode = ParseMode.HTML
}

/**
 * HTML text, represents the case when there is no parse mode, but entities are set.
 */
data class TextWithEntities(
    override val text: String,
    override val entities: List<MessageEntity> = listOf(),
) : RichText

