package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for [BotCommandScope.type].
 */
enum class BotCommandScopeType {
    @TgEnumName(DEFAULT_STR)
    DEFAULT,
    @TgEnumName(ALL_PRIVATE_CHATS_STR)
    ALL_PRIVATE_CHATS,
    @TgEnumName(ALL_GROUP_CHATS_STR)
    ALL_GROUP_CHATS,
    @TgEnumName(ALL_CHAT_ADMINISTRATORS_STR)
    ALL_CHAT_ADMINISTRATORS,
    @TgEnumName(CHAT_STR)
    CHAT,
    @TgEnumName(CHAT_ADMINISTRATORS_STR)
    CHAT_ADMINISTRATORS,
    @TgEnumName(CHAT_MEMBER_STR)
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
