package ski.gagar.vertigram.samples

import ski.gagar.vertigram.telegram.markup.inlineKeyboard
import ski.gagar.vertigram.telegram.markup.keyboard

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
            requestLocation("Request location")
        }
    }
}