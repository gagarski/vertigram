package ski.gagar.vxutil.vertigram.types.attachments

import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.binaryFileUploadIfNotNull
import java.io.File

data class FileAttachment(val file: File) : Attachment {
    override fun attachDirectly(form: MultipartForm, field: String) {
        form.binaryFileUploadIfNotNull(field, file)
    }

    override fun attachIndirectly(form: MultipartForm, field: String) {
        form.binaryFileUploadIfNotNull(field, file)
    }

    override fun getIndirectUrl(field: String): String = "attach://$field"

    override fun getIndirectAttachment(field: String): UrlAttachment = UrlAttachment(getIndirectUrl(field))
}
