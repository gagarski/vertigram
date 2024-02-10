package ski.gagar.vertigram.richtext

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
    JsonSubTypes.Type(value = MarkdownText::class, name = ParseMode.MARKDOWN_STR),
    JsonSubTypes.Type(value = MarkdownV2Text::class, name = ParseMode.MARKDOWN_V2_STR),
    JsonSubTypes.Type(value = HtmlText::class, name = ParseMode.HTML_STR),
)
sealed interface RichText

@Suppress("DEPRECATION")
@Deprecated("Consider using other mode", replaceWith = ReplaceWith("MarkdownV2Text"))
data class MarkdownText(
    val text: String
) : RichText {
    val parseMode: ParseMode = ParseMode.MARKDOWN
}

data class MarkdownV2Text(
    val text: String
) : RichText {
    val parseMode: ParseMode = ParseMode.MARKDOWN_V2
}

data class HtmlText(
    val text: String
) : RichText {
    val parseMode: ParseMode = ParseMode.HTML
}


data class TextWithEntities(
    val text: String,
    val entities: List<MessageEntity> = listOf(),
) : RichText

