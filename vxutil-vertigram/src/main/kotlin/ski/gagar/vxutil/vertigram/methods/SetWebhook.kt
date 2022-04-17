package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.UpdateType
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
import kotlin.math.max

@TgMethod
data class SetWebhook(
    val url: String,
    val certificate: Attachment? = null,
    val ipAddress: String? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<UpdateType>? = null,
    val dropPendingUpdates: Boolean = false
) : MultipartTgCallable<Boolean> {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("url", url)
        attachDirectly("certificate", certificate)
        attributeIfNotNull("ip_address", ipAddress)
        attributeIfNotNull("max_connections", maxConnections)
        jsonAttributeIfNotNull("allowed_updates", allowedUpdates)
        attributeIfTrue("drop_pending_updates", dropPendingUpdates)
    }
}
