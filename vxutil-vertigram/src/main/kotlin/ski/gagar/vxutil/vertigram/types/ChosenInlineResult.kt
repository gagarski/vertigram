package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ChosenInlineResult.
 */
data class ChosenInlineResult(
    val resultId: String,
    val from: User,
    val location: Location? = null,
    val inlineMessageId: String? = null,
    val query: String
)
