package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineKeyboardMarkup.
 */
data class InlineKeyboardMarkup(
    val inlineKeyboard: List<List<InlineKeyboardButton>>
) : ReplyMarkup()


