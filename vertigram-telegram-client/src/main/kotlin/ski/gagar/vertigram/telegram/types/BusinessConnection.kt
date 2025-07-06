package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Telegram [BusinessConnection](https://core.telegram.org/bots/api#businessconnection) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BusinessConnection internal constructor(
    val id: String,
    val user: User,
    val userChatId: Long,
    val date: Instant,
    val rights: BotRights,
    @get:JvmName("getIsEnabled")
    val isEnabled: Boolean
) {
    /**
     * Telegram [BusinessBotRights](https://core.telegram.org/bots/api#businessbotrights) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class BotRights internal constructor(
        val canReply: Boolean = false,
        val canReadMessages: Boolean = false,
        val canDeleteSentMessages: Boolean = false,
        val canDeleteAllMessages: Boolean = false,
        val canEditName: Boolean = false,
        val canEditBio: Boolean = false,
        val canEditProfilePhoto: Boolean = false,
        val canEditUsername: Boolean = false,
        val canChangeGiftSettings: Boolean = false,
        val canViewGiftsAndStars: Boolean = false,
        val canConvertGiftsToStars: Boolean = false,
        val canTransferAndUpgradeGifts: Boolean = false,
        val canTransferStars: Boolean = false,
        val canManageStories: Boolean = false,
    ) {
        companion object
    }

    companion object


}
