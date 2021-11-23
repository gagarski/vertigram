package ski.gagar.vxutil.vertigram.entities

data class ChosenInlineResult(
    val resultId: String,
    val from: User,
    val query: String,
    val location: Location? = null,
    val inlineMessageId: String? = null
)
