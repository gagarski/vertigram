package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class BotCommandScopeType {
    @JsonProperty(DEFAULT_STR)
    DEFAULT,
    @JsonProperty(ALL_PRIVATE_CHATS_STR)
    ALL_PRIVATE_CHATS,
    @JsonProperty(ALL_GROUP_CHATS_STR)
    ALL_GROUP_CHATS,
    @JsonProperty(ALL_CHAT_ADMINISTRATORS_STR)
    ALL_CHAT_ADMINISTRATORS,
    @JsonProperty(CHAT_STR)
    CHAT,
    @JsonProperty(CHAT_ADMINISTRATORS_STR)
    CHAT_ADMINISTRATORS,
    @JsonProperty(CHAT_MEMBER_STR)
    CHAT_MEMBER;

    companion object {
        const val DEFAULT_STR = "default"
        const val ALL_PRIVATE_CHATS_STR = "all_private_chats"
        const val ALL_GROUP_CHATS_STR = "all_group_chats"
        const val ALL_CHAT_ADMINISTRATORS_STR = "all_chat_administrators"
        const val CHAT_STR = "chat_str"
        const val CHAT_ADMINISTRATORS_STR = "chat_administrators"
        const val CHAT_MEMBER_STR = "chat_member"
    }
}
