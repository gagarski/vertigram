package ski.gagar.vxutil.vertigram.types

data class ChosenInlineResult(
    val resultId: String,
    val from: User,
    val location: Location? = null,
    val inlineMessageId: String? = null,
    val query: String
)
