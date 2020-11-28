package ski.gagar.vertigram.entities

data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val location: Location? = null
)