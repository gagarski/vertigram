package ski.gagar.vxutil.vertigram.types

data object BotCommandScopeAllPrivateChats : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.ALL_PRIVATE_CHATS
}
