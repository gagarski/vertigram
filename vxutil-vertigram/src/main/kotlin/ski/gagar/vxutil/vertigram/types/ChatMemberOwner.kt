package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore

data class ChatMemberOwner(
    override val user: User,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    val customTitle: String? = null
) : ChatMember {
    override val status: ChatMemberStatus = ChatMemberStatus.OWNER
    @JsonIgnore
    override val isMember: Boolean = true
}
