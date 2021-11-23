package ski.gagar.vxutil.vertigram.entities

data class CallbackQuery(
    val id: String,
    val from: User,
    val chatInstance: String,
    val message: Message? = null,
    val inlineMessageId: String? = null,
    val data: String? = null,
    val gameShortName: String? = null
)
