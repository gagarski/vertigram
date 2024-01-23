package ski.gagar.vxutil.vertigram.types

data class GiveawayCompleted(
    val winnerCount: Int,
    val unclaimedPrizeCount: Int? = null,
    val giveawayMessage: Message? = null
)
