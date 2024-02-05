package ski.gagar.vertigram.types

data class WriteAccessAllowed(
    // Since Telegram Bot API 6.7
    val webAppName: String? = null,
    // Since Telegram Bot API 6.9
    val fromRequest: Boolean = false,
    val fromAttachmentMenu: Boolean = false
)
