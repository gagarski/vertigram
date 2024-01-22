package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class Giveaway(
    val chats: List<Chat>,
    val winnerSelectionDate: Instant,
    val winnerCount: Int,
    val onlyNewMembers: Boolean = false,
    val hasPublicWinners: Boolean = false,
    val prizeDescription: String? = null,
    val countryCodes: List<String> = listOf(),
    val premiumSubscriptionMonthCount: Int? = null
)
