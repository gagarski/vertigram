package ski.gagar.vertigram.samples

import ski.gagar.vertigram.telegram.markup.inlineKeyboard
import ski.gagar.vertigram.telegram.markup.keyboard
import ski.gagar.vertigram.telegram.markup.richMessageHtml
import ski.gagar.vertigram.telegram.markup.richMessageMarkdown

/**
 * A sample for [inlineKeyboard] for documentation purposes
 */
private fun inlineKeyboardSample() {
    inlineKeyboard {
        row {
            text(text = "Button 1")
            url(text = "Button 2", url = "https://example.com")
        }

        row {
            switchInline(text = "Button 3", switchInlineQuery = "bla")
            callback(text = "Button 4", callbackData = "qweqwe")
        }
    }
}

/**
 * A sample for [keyboard] for documentation purposes
 */
private fun keyboardSample() {
    keyboard {
        row {
            text(text = "Simple text")
        }
        row {
            requestUsers(text = "Request users", requestId = 1)
            requestLocation(text = "Request location")
        }
    }
}

/**
 * A sample for [richMessageHtml] for documentation purposes
 */
private fun richMessageSample() {
    richMessageHtml {
        h1 { +"Weekly report" }
        paragraph {
            +"Build status: "
            b { +"green" }
        }
        unorderedList {
            item(checked = true) {
                paragraph { +"Bot API 10.1 types added" }
            }
            item {
                paragraph { +"Rich message builders available" }
            }
        }
        photo("https://example.com/chart.png") {
            +"Coverage trend"
            credit { +"CI dashboard" }
        }
    }
}

/**
 * A sample for [richMessageMarkdown] for documentation purposes
 */
private fun richMessageMarkdownSample() {
    richMessageMarkdown {
        h1 { +"Weekly report" }
        paragraph {
            +"Build status: "
            b { +"green" }
        }
    }
}
