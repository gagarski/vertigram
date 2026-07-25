package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Describes the connection of the bot with a business account.
 *
 * See Telegram's [BusinessConnection](https://core.telegram.org/bots/api#businessconnection) documentation.
 */
@TelegramCodegen.Type
data class BusinessConnection internal constructor(
    /** Unique identifier of the business connection. */
    val id: String,
    /** Business account user that created the business connection. */
    val user: User,
    /** Identifier of a private chat with the user who created the business connection. */
    val userChatId: Long,
    /** Date the connection was established. */
    val date: Instant,
    /** Rights of the business bot. */
    val rights: BotRights,
    /** `true` if the connection is active. */
    @get:JvmName("getIsEnabled")
    val isEnabled: Boolean
) {
    /**
     * Represents the rights of a business bot.
     *
     * See Telegram's [BusinessBotRights](https://core.telegram.org/bots/api#businessbotrights) documentation.
     */
    @TelegramCodegen.Type
    data class BotRights internal constructor(
        /**
         * `true` if the bot can send and edit messages in private chats that had incoming messages in the last 24
         * hours.
         */
        val canReply: Boolean = false,
        /** `true` if the bot can mark incoming private messages as read. */
        val canReadMessages: Boolean = false,
        /** `true` if the bot can delete messages sent by the bot. */
        val canDeleteSentMessages: Boolean = false,
        /** `true` if the bot can delete all private messages in managed chats. */
        val canDeleteAllMessages: Boolean = false,
        /** `true` if the bot can edit the first and last name of the business account. */
        val canEditName: Boolean = false,
        /** `true` if the bot can edit the bio of the business account. */
        val canEditBio: Boolean = false,
        /** `true` if the bot can edit the profile photo of the business account. */
        val canEditProfilePhoto: Boolean = false,
        /** `true` if the bot can edit the username of the business account. */
        val canEditUsername: Boolean = false,
        /** `true` if the bot can change the privacy settings pertaining to gifts for the business account. */
        val canChangeGiftSettings: Boolean = false,
        /** `true` if the bot can view gifts and the amount of Telegram Stars owned by the business account. */
        val canViewGiftsAndStars: Boolean = false,
        /** `true` if the bot can convert regular gifts owned by the business account to Telegram Stars. */
        val canConvertGiftsToStars: Boolean = false,
        /** `true` if the bot can transfer and upgrade gifts owned by the business account. */
        val canTransferAndUpgradeGifts: Boolean = false,
        /**
         * `true` if the bot can transfer Telegram Stars received by the business account to its own account, or use
         * them to upgrade and transfer gifts.
         */
        val canTransferStars: Boolean = false,
        /** `true` if the bot can post, edit and delete stories on behalf of the business account. */
        val canManageStories: Boolean = false,
    ) {
        companion object
    }

    companion object


}
