package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ChatMemberLeft.
 */
data class ChatMemberLeft(
    override val user: User
) : ChatMember() {
    override val status: ChatMemberStatus = ChatMemberStatus.LEFT
    override val isMember: Boolean = false
}
