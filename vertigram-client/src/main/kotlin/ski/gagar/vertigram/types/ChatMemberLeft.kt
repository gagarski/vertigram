package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore

data class ChatMemberLeft(
    override val user: User
) : ChatMember {
    override val status: ChatMemberStatus = ChatMemberStatus.LEFT
    @JsonIgnore
    override val isMember: Boolean = false
}
