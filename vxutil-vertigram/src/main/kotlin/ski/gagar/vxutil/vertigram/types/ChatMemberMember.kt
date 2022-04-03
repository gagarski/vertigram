package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ChatMemberMember.
 */
data class ChatMemberMember(
    override val user: User
) : ChatMember() {
    override val status: ChatMemberStatus = ChatMemberStatus.MEMBER
    override val isMember: Boolean = true
}
