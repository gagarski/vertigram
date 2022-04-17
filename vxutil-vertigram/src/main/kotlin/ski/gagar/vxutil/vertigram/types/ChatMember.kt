package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "status")
@JsonSubTypes(
    JsonSubTypes.Type(value = ChatMemberOwner::class, name = ChatMemberStatus.OWNER_STR),
    JsonSubTypes.Type(value = ChatMemberAdministrator::class, name = ChatMemberStatus.ADMINISTRATOR_STR),
    JsonSubTypes.Type(value = ChatMemberMember::class, name = ChatMemberStatus.MEMBER_STR),
    JsonSubTypes.Type(value = ChatMemberRestricted::class, name = ChatMemberStatus.RESTRICTED_STR),
    JsonSubTypes.Type(value = ChatMemberLeft::class, name = ChatMemberStatus.LEFT_STR),
    JsonSubTypes.Type(value = ChatMemberBanned::class, name = ChatMemberStatus.BANNED_STR),
)
sealed interface ChatMember {
    val status: ChatMemberStatus
    @get:JsonIgnore
    val isMember: Boolean
    val user: User
}

