package ski.gagar.vertigram.types

data class KeyboardButtonRequestChat(
    val requestId: Long,
    val chatIsChannel: Boolean,
    val chatIsForum: Boolean? = false,
    val chatHasUsername: Boolean? = false,
    val chatIsCreated: Boolean? = false,
    val userAdministratorRights: ChatAdministratorRights? = ChatAdministratorRights(),
    val botAdministratorRights: ChatAdministratorRights? = ChatAdministratorRights(),
    val botIsMember: Boolean? = false
)
