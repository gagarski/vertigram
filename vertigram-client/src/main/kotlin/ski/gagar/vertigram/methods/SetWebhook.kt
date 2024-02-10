package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.UpdateType
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.annotations.TelegramMedia

data class SetWebhook(
    val url: String,
    @TelegramMedia
    val certificate: Attachment? = null,
    val ipAddress: String? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<UpdateType>? = null,
    val dropPendingUpdates: Boolean = false,
    val secretToken: String? = null
) : MultipartTelegramCallable<Boolean>()
