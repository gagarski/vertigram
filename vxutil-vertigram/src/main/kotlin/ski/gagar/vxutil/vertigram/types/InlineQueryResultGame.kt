package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultGame
 */
data class InlineQueryResultGame(
    val id: String,
    val gameShortName: String,
    val replyMarkup: InlineKeyboardMarkup? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.GAME
}
