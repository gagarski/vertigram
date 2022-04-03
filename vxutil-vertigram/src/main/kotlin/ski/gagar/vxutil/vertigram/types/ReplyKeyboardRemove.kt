package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ReplyKeyboardRemove
 */
data class ReplyKeyboardRemove(
    val removeKeyboard: Boolean = false,
    val selective: Boolean = false
) : ReplyMarkup()
