package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Available values for all parseMode fields.
 */
enum class ParseMode {
    @JsonProperty("Markdown")
    MARKDOWN,
    @JsonProperty("MarkdownV2")
    MARKDOWN_V2,
    @JsonProperty("HTML")
    HTML
}
