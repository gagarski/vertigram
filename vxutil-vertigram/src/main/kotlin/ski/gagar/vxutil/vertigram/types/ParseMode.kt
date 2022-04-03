package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for all parseMode fields.
 */
enum class ParseMode {
    @TgEnumName("Markdown")
    MARKDOWN,
    @TgEnumName("MarkdownV2")
    MARKDOWN_V2,
    @TgEnumName("HTML")
    HTML
}
