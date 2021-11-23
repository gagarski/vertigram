package ski.gagar.vxutil.vertigram.entities

import ski.gagar.vxutil.vertigram.util.TgEnumName

enum class ParseMode {
    @TgEnumName("Markdown")
    MARKDOWN,
    @TgEnumName("MarkdownV2")
    MARKDOWN_V2,
    @TgEnumName("HTML")
    HTML
}
