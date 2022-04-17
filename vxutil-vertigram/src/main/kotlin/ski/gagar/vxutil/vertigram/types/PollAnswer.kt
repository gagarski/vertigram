package ski.gagar.vxutil.vertigram.types

data class PollAnswer(
    val pollId: String,
    val user: User,
    val optionIds: List<Int>
)
