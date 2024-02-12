package ski.gagar.vertigram.types

data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val chatType: Chat.Type? = null,
    val location: Location? = null
)
