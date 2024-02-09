package ski.gagar.vertigram.richtext

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.*

@Suppress("DEPRECATION")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "parseMode",
    defaultImpl = CaptionWithEntities::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = MarkdownCaption::class, name = ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownV2Caption::class, name = ParseMode.MARKDOWN_V2_STR),
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
        fun from(caption: String,
                 parseMode: ParseMode?,
                 captionEntities: List<MessageEntity>?) : RichCaption {
            if (parseMode != null) require(null == captionEntities)
            return when (parseMode) {
                ParseMode.MARKDOWN -> MarkdownCaption(caption)
                ParseMode.MARKDOWN_V2 -> MarkdownV2Caption(caption)
                ParseMode.HTML -> HtmlCaption(caption)
                else -> CaptionWithEntities(caption, captionEntities!!)
            }
        }
    }
}

@Suppress("DEPRECATION")
@Deprecated("Consider using other mode", replaceWith = ReplaceWith("MarkdownV2Caption"))
data class MarkdownCaption(
    override val caption: String
) : RichCaption {
    override val parseMode: ParseMode = ParseMode.MARKDOWN
}

data class MarkdownV2Caption(
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

