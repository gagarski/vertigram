package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type BotCommandScopeChatAdministrators.
 */
data class BotCommandScopeChatAdministrators(
    val chatId: ChatId
) : BotCommandScope() {
    override val type: BotCommandScopeType = BotCommandScopeType.CHAT_ADMINISTRATORS
}
