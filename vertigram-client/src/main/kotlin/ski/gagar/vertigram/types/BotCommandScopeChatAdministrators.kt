package ski.gagar.vertigram.types

data class BotCommandScopeChatAdministrators(
    val chatId: ChatId
) : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.CHAT_ADMINISTRATORS
}
