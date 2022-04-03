package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQuery.
 */
data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val chatType: ChatType? = null,
    val location: Location? = null
)
