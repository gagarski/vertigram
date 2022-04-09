package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ChatMemberOwner.
 */
data class ChatMemberOwner(
    override val user: User,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean,
    val customTitle: String? = null
) : ChatMember() {
    override val status: ChatMemberStatus = ChatMemberStatus.OWNER
    override val isMember: Boolean = true
}
