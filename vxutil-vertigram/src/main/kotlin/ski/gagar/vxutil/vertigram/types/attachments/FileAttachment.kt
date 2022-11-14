package ski.gagar.vxutil.vertigram.types.attachments

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.kotlin.coroutines.await
import ski.gagar.vxutil.web.multipart.FilePart
import java.io.File

data class FileAttachment(val file: File) : Attachment {
    override fun getReference(referredField: String): UrlAttachment = UrlAttachment("attach://$referredField")
    override fun getReferredPart(field: String, vertx: Vertx) = doAttach(field, vertx)

    override fun getPart(field: String, vertx: Vertx): FilePart = doAttach(field, vertx)

    private fun doAttach(field: String, vertx: Vertx): FilePart =
        FilePart(field, file.toPath().fileName.toString(), { vertx.fileSystem().open(file.path, OpenOptions()).await() } )
}
