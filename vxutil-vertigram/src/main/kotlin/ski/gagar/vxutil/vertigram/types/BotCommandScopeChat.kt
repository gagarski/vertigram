package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type BotCommandScopeChat.
 */
data class BotCommandScopeChat(
    val chatId: String
) : BotCommandScope() {
    override val type: BotCommandScopeType = BotCommandScopeType.CHAT
}

/**
 * A utility function to create [BotCommandScopeChat] with integral chatId.
 */
fun BotCommandScopeChat(
    chatId: Long
) = BotCommandScopeChat(chatId.toString())
