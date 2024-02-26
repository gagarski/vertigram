package ski.gagar.vertigram.telegram.types.richtext

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.telegram.types.MessageEntity

/**
 * A convenience type which replaces triples like `caption`/`captionEntities`/`parseMode` whith a type-safe wrapper.
 *
 * Consider using builders from [ski.gagar.vertigram.telegram.markup] package to create the instances:
 *  - [ski.gagar.vertigram.telegram.markup.textMarkdown] for [MarkdownV2Text]
 *  - [ski.gagar.vertigram.telegram.markup.textHtml] for [HtmlText]
 *  - [ski.gagar.vertigram.telegram.markup.textWithEntities] for [TextWithEntities]
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
    JsonSubTypes.Type(value = MarkdownText::class, name = RichText.ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownV2Text::class, name = RichText.ParseMode.MARKDOWN_V2_STR),
    JsonSubTypes.Type(value = HtmlText::class, name = RichText.ParseMode.HTML_STR),
    JsonSubTypes.Type(value = TextWithEntities::class, name = RichText.ParseMode.HTML_STR),
)
sealed interface RichText {
    val text: String
    @get:JsonIgnore
    val parseMode: ParseMode?
        get() = null
    @get:JsonIgnore
    val entities: List<MessageEntity>?
        get() = null

    /**
     * Value for `...parseMode` fields.
     */
    enum class ParseMode {
        @JsonProperty(MARKDOWN_STR)
        @Deprecated("Consider using other mode", replaceWith = ReplaceWith("MARKDOWN_V2"))
        MARKDOWN,
        @JsonProperty(MARKDOWN_V2_STR)
        MARKDOWN_V2,
        @JsonProperty(HTML_STR)
        HTML;

        companion object {
            const val MARKDOWN_STR = "Markdown"
            const val MARKDOWN_V2_STR = "MarkdownV2"
            const val HTML_STR = "HTML"
        }
    }


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
    override val parseMode: RichText.ParseMode = RichText.ParseMode.MARKDOWN
}

/**
 * Markdown v. 2 text, represents the case when `parseMode` is `MarkdownV2`.
 */
data class MarkdownV2Text(
    override val text: String
) : RichText {
    override val parseMode: RichText.ParseMode = RichText.ParseMode.MARKDOWN_V2
}

/**
 * HTML text, represents the case when `parseMode` is `HTML`.
 */
data class HtmlText(
    override val text: String
) : RichText {
    override val parseMode: RichText.ParseMode = RichText.ParseMode.HTML
}

/**
 * HTML text, represents the case when there is no parse mode, but entities are set.
 */
data class TextWithEntities(
    override val text: String,
    override val entities: List<MessageEntity> = listOf(),
) : RichText

