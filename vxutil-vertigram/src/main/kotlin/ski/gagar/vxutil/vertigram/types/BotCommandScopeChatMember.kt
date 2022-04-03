package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type BotCommandScopeChatMember.
 */
data class BotCommandScopeChatMember(
    val chatId: String,
    val userId: Long
)

/**
 * A utility function to create [BotCommandScopeChatMember] with integral chatId.
 */
fun BotCommandScopeChatMember(
    chatId: Long,
    userId: Long
) = BotCommandScopeChatMember(chatId.toString(), userId)
