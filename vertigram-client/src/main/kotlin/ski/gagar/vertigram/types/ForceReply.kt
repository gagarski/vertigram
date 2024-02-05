package ski.gagar.vertigram.types

data class ForceReply(
    val forceReply: Boolean = false,
    val inputFieldPlaceholder: String? = null,
    val selective: Boolean = false
) : ReplyMarkup
