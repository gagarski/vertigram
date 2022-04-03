package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type BotCommandScopeChatAdministrators.
 */
data class BotCommandScopeChatAdministrators(
    val chatId: String
) : BotCommandScope() {
    override val type: BotCommandScopeType = BotCommandScopeType.CHAT_ADMINISTRATORS
}

/**
 * A utility function to create [BotCommandScopeChatAdministrators] with integral chatId.
 */
fun BotCommandScopeChatAdministrators(
    chatId: Long
) = BotCommandScopeChatAdministrators(chatId.toString())
