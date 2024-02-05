package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class ParseMode {
    @JsonProperty("Markdown")
    @Deprecated("Consider using other mode", replaceWith = ReplaceWith("MARKDOWN_V2"))
    MARKDOWN,
    @JsonProperty("MarkdownV2")
    MARKDOWN_V2,
    @JsonProperty("HTML")
    HTML
}
