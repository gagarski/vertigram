package ski.gagar.vxutil.vertigram.types

data object BotCommandScopeAllChatAdministrators : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.ALL_CHAT_ADMINISTRATORS
}
