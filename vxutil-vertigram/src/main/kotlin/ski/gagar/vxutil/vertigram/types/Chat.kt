package ski.gagar.vxutil.vertigram.types

data class Chat(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    // Since Telegram Bot API 6.3
    @get:JvmName("getIsForum")
    val isForum: Boolean = false
)

