package ski.gagar.vertigram.types

data object BotCommandScopeAllGroupChats : BotCommandScope {
    override val type: BotCommandScopeType = BotCommandScopeType.ALL_GROUP_CHATS
}
