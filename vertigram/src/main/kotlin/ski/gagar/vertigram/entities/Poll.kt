package ski.gagar.vertigram.entities

data class PollOption(
    val text: String,
    val voterCount: Long
)

data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    @get:JvmName("getIsClosed")
    val isClosed: Boolean
)