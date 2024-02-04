package ski.gagar.vertigram.types

data class ReplyKeyboardRemove(
    val removeKeyboard: Boolean = false,
    val selective: Boolean = false
) : ReplyMarkup

