package ski.gagar.vxutil.vertigram.types

data class PollAnswer(
    val pollId: String,
    val optionIds: List<Int>,
    val user: User? = null,
    // Since Telegram Bot API 6.8
    val voterChat: Chat? = null
)
