package ski.gagar.vertigram.types

data class ChatBoostSourcePremium(val user: User) : ChatBoostSource {
    override val source: ChatBoostSourceType = ChatBoostSourceType.PREMIUM
}