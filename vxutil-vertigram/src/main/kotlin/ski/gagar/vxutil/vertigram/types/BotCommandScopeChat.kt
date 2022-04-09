package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type BotCommandScopeChat.
 */
data class BotCommandScopeChat(
    val chatId: ChatId
) : BotCommandScope() {
    override val type: BotCommandScopeType = BotCommandScopeType.CHAT
}
