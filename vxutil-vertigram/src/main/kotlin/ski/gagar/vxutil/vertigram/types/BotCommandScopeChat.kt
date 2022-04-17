package ski.gagar.vxutil.vertigram.types

data class BotCommandScopeChat(
    val chatId: ChatId
) : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.CHAT
}
