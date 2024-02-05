package ski.gagar.vertigram.types

data class TextQuote(
    val text: String,
    val position: Int,
    val entities: List<MessageEntity>? = null,
    @get:JvmName("getIsManual")
    val isManual: Boolean = false
)
