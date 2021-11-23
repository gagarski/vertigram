package ski.gagar.vxutil.vertigram.entities

data class ChatPermissions(
    // All permissions are optional by specification. Empty value is sent as false and treated as false
    val canSendMessages: Boolean = false,
    val canSendMediaMessages: Boolean = false,
    val canSendPolls: Boolean = false,
    val canSendOtherMessages: Boolean = false,
    val canAddWebPagePreviews: Boolean = false,
    val canChangeInfo: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canPinMessages: Boolean = false
)
