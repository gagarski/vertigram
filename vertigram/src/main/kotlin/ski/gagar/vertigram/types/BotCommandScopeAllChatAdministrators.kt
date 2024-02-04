package ski.gagar.vertigram.types

data object BotCommandScopeAllChatAdministrators : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.ALL_CHAT_ADMINISTRATORS
}
