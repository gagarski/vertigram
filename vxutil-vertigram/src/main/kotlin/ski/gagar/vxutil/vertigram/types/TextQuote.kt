package ski.gagar.vxutil.vertigram.types

data class TextQuote(
    val text: String,
    val entities: List<MessageEntity>,
    val position: Int,
    @get:JvmName("getIsManual")
    val isManual: Boolean = false
)
