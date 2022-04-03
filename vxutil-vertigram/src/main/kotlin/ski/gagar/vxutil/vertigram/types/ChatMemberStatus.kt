package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for [ChatMember.status]
 */
enum class ChatMemberStatus {
    @TgEnumName(OWNER_STR)
    OWNER,
    @TgEnumName(ADMINISTRATOR_STR)
    ADMINISTRATOR,
    @TgEnumName(MEMBER_STR)
    MEMBER,
    @TgEnumName(RESTRICTED_STR)
    RESTRICTED,
    @TgEnumName(LEFT_STR)
    LEFT,
    @TgEnumName(BANNED_STR)
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
