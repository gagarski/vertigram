package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

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
