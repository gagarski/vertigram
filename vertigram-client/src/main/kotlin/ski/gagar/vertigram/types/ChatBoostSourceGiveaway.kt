package ski.gagar.vertigram.types

data class ChatBoostSourceGiveaway(
    val giveAwayMessageId: Long,
    val user: User,
    @get:JvmName("getIsUnclaimed")
    val isUnclaimed: Boolean = false
) : ChatBoostSource {
    override val source: ChatBoostSourceType = ChatBoostSourceType.GIVEAWAY
}