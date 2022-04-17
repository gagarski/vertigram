package ski.gagar.vxutil.vertigram.types

data class ChatPermissions(
    val canSendMessages: Boolean = false,
    val canSendMediaMessages: Boolean = false,
    val canSendPolls: Boolean = false,
    val canSendOtherMessages: Boolean = false,
    val canAddWebPagePreviews: Boolean = false,
    val canChangeInfo: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canPinMessages: Boolean = false
)
