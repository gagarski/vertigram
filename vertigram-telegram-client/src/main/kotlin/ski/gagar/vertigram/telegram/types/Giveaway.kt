package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * This object represents a message about a scheduled giveaway.
 *
 * See Telegram's [Giveaway](https://core.telegram.org/bots/api#giveaway) documentation.
 */
@TelegramCodegen.Type
data class Giveaway internal constructor(
    /** The list of chats which the user must join to participate in the giveaway. */
    val chats: List<Chat>,
    /** Point in time when winners of the giveaway will be selected. */
    val winnersSelectionDate: Instant,
    /** The number of users which are supposed to be selected as winners of the giveaway. */
    val winnerCount: Int,
    /** `true` if only users who join the chats after the giveaway started should be eligible to win. */
    val onlyNewMembers: Boolean = false,
    /** `true` if the list of giveaway winners will be visible to everyone. */
    val hasPublicWinners: Boolean = false,
    /** Description of additional giveaway prize. */
    val prizeDescription: String? = null,
    /**
     * A list of two-letter ISO 3166-1 alpha-2 country codes indicating the countries from which eligible users must
     * come. If empty, all users can participate. Users with a phone number bought on Fragment can always participate.
     */
    val countryCodes: List<String> = listOf(),
    /** The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only. */
    val prizeStarCount: Int? = null,
    /**
     * The number of months the Telegram Premium subscription won from the giveaway will be active for; for Telegram
     * Premium giveaways only.
     */
    val premiumSubscriptionMonthCount: Int? = null
) {
    /**
     * This object represents a message about the completion of a giveaway with public winners.
     *
     * See Telegram's [GiveawayWinners](https://core.telegram.org/bots/api#giveawaywinners) documentation.
     */
    @TelegramCodegen.Type
    data class Winners internal constructor(
        /** The chat that created the giveaway. */
        val chat: Chat,
        /** Identifier of the message with the giveaway in the chat. */
        val giveawayMessageId: Long,
        /** Point in time when winners of the giveaway were selected. */
        val winnersSelectionDate: Instant,
        /** Total number of winners in the giveaway. */
        val winnerCount: Int,
        /** List of up to 100 winners of the giveaway. */
        val winners: List<User>,
        /** The number of other chats the user had to join to be eligible for the giveaway. */
        val additionalChatCount: Int? = null,
        /** The number of Telegram Stars that were split between winners; for Telegram Star giveaways only. */
        val prizeStarCount: Int? = null,
        /**
         * The number of months the Telegram Premium subscription won from the giveaway will be active for; for
         * Telegram Premium giveaways only.
         */
        val premiumSubscriptionMonthCount: Int? = null,
        /** Number of undistributed prizes. */
        val unclaimedPrizeCount: Int? = null,
        /** `true` if only users who joined the chats after the giveaway started were eligible to win. */
        val onlyNewMembers: Boolean = false,
        /** `true` if the giveaway was canceled because the payment for it was refunded. */
        val wasRefunded: Boolean = false,
        /** Description of additional giveaway prize. */
        val prizeDescription: String? = null
    ) {
        companion object
    }

    companion object
}
