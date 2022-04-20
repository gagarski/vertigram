package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.UpdateType
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia

@TgMethod
data class SetWebhook(
    val url: String,
    @TgMedia
    val certificate: Attachment? = null,
    val ipAddress: String? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<UpdateType>? = null,
    val dropPendingUpdates: Boolean = false
) : MultipartTgCallable<Boolean>()
