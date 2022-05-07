package ski.gagar.vxutil.vertigram.types

data class BotCommandScopeChatMember(
    val chatId: ChatId,
    val userId: Long
) : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.CHAT_MEMBER
}

