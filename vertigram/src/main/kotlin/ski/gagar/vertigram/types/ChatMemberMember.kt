package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore

data class ChatMemberMember(
    override val user: User
) : ChatMember {
    override val status: ChatMemberStatus = ChatMemberStatus.MEMBER
    @JsonIgnore
    override val isMember: Boolean = true
}
