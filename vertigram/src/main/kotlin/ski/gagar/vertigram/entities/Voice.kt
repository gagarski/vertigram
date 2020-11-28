package ski.gagar.vertigram.entities

data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Long,
    val mimeType: String?,
    val fileSize: Long?
)