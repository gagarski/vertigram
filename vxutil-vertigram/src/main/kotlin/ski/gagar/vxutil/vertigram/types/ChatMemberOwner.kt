package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ChatMemberOwner.
 */
data class ChatMemberOwner(
    override val user: User,
    val isAnonymous: Boolean,
    val customTitle: String
) : ChatMember() {
    override val status: ChatMemberStatus = ChatMemberStatus.OWNER
    override val isMember: Boolean = true
}
