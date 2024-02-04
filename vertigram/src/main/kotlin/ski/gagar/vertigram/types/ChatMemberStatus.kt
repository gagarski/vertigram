package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class ChatMemberStatus {
    @JsonProperty(OWNER_STR)
    OWNER,
    @JsonProperty(ADMINISTRATOR_STR)
    ADMINISTRATOR,
    @JsonProperty(MEMBER_STR)
    MEMBER,
    @JsonProperty(RESTRICTED_STR)
    RESTRICTED,
    @JsonProperty(LEFT_STR)
    LEFT,
    @JsonProperty(BANNED_STR)
    BANNED;

    companion object {
        const val OWNER_STR = "creator"
        const val ADMINISTRATOR_STR = "administrator"
        const val MEMBER_STR = "member"
        const val RESTRICTED_STR = "restricted"
        const val LEFT_STR = "left"
        const val BANNED_STR = "kicked"
    }
}
