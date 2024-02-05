package ski.gagar.vertigram.types

data class SwitchInlineQueryChosenChat(
    val query: String? = null,
    val allowUserChats: Boolean = false,
    val allowBotChats: Boolean = false,
    val allowGroupChats: Boolean = false,
    val allowChannelChats: Boolean = false
)
