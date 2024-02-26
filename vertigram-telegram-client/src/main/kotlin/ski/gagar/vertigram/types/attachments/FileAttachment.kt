package ski.gagar.vertigram.types.attachments

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.kotlin.coroutines.coAwait
import ski.gagar.vertigram.web.multipart.FilePart
import java.io.File

/**
 * An implementation of [Attachment] allowing to attach a local filesystem file.
 */
data class FileAttachment(val file: File) : AbstractFileAttachment() {
    override fun doAttach(field: String, vertx: Vertx): FilePart =
        FilePart(field, file.toPath().fileName.toString(), { vertx.fileSystem().open(file.path, OpenOptions()).coAwait() } )
}
