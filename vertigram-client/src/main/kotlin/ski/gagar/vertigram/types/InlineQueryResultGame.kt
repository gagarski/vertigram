package ski.gagar.vertigram.types

data class InlineQueryResultGame(
    val id: String,
    val gameShortName: String,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.GAME
}
