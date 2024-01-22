package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class GiveawayWinners(
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
