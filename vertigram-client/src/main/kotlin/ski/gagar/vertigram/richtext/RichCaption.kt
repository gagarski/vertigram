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
    defaultImpl = CaptionWithEntities::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MarkdownV1Caption::class, name = ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownCaption::class, name = ParseMode.MARKDOWN_V2_STR),
    JsonSubTypes.Type(value = HtmlCaption::class, name = ParseMode.HTML_STR),
)
sealed interface RichCaption {
    val caption: String
    @get:JsonIgnore
    val parseMode: ParseMode?
        get() = null
    @get:JsonIgnore
    val captionEntities: List<MessageEntity>?
        get() = null
    companion object {
        operator fun invoke(caption: String,
                            parseMode: ParseMode?,
                            captionEntities: List<MessageEntity>?) : RichCaption {
            if (parseMode != null) require(null == captionEntities)
            return when (parseMode) {
                ParseMode.MARKDOWN -> MarkdownV1Caption(caption)
                ParseMode.MARKDOWN_V2 -> MarkdownCaption(caption)
                ParseMode.HTML -> HtmlCaption(caption)
                else -> CaptionWithEntities(caption, captionEntities ?: listOf())
            }
        }
    }
}

@Suppress("DEPRECATION")
@Deprecated("Consider using other mode", replaceWith = ReplaceWith("MarkdownCaption"))
data class MarkdownV1Caption(
    override val caption: String
) : RichCaption {
    override val parseMode: ParseMode = ParseMode.MARKDOWN
}

data class MarkdownCaption(
    override val caption: String
) : RichCaption {
    override val parseMode: ParseMode = ParseMode.MARKDOWN_V2
}

data class HtmlCaption(
    override val caption: String
) : RichCaption {
    override val parseMode: ParseMode = ParseMode.HTML
}


data class CaptionWithEntities(
    override val caption: String,
    override val captionEntities: List<MessageEntity> = listOf(),
) : RichCaption

