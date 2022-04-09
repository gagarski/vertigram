package ski.gagar.vxutil.vertigram.types.attachments

import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.web.attributeIfNotNull

data class UrlAttachment(val url: String) : Attachment {
    override fun attachDirectly(form: MultipartForm, field: String) {
        form.attributeIfNotNull(field, url)
    }

    override fun attachIndirectly(form: MultipartForm, field: String) {}

    override fun getIndirectUrl(field: String) = url

    override fun getIndirectAttachment(field: String) = this
}
