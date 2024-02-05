package ski.gagar.vertigram.types

data class CallbackQuery(
    val id: String,
    val from: User,
    val message: Message? = null,
    val inlineMessageId: String? = null,
    val chatInstance: String,
    val data: String? = null,
    val gameShortName: String? = null
)
