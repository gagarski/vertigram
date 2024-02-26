package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [Giveaway](https://core.telegram.org/bots/api#giveaway) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Giveaway(
    val chats: List<Chat>,
    val winnersSelectionDate: Instant,
    val winnerCount: Int,
    val onlyNewMembers: Boolean = false,
    val hasPublicWinners: Boolean = false,
    val prizeDescription: String? = null,
    val countryCodes: List<String> = listOf(),
    val premiumSubscriptionMonthCount: Int? = null
) {
    /**
     * Telegram [GiveawayWinners](https://core.telegram.org/bots/api#giveawaywinners) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Winners(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val chat: Chat,
        val giveawayMessageId: Long,
        val winnersSelectionDate: Instant,
        val winnerCount: Int,
        val winners: List<User>,
        val additionalChatCount: Int? = null,
        val premiumSubscriptionMonthCount: Int? = null,
        val unclaimedPrizeCount: Int? = null,
        val onlyNewMembers: Boolean = false,
        val wasRefunded: Boolean = false,
        val prizeDescription: String? = null
    )

}
