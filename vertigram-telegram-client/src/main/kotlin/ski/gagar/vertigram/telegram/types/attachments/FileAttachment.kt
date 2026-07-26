package ski.gagar.vertigram.telegram.types.attachments

import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.kotlin.coroutines.coAwait
import ski.gagar.vertigram.web.multipart.FilePart
import java.io.File

/**
 * An [Attachment] backed by a file visible to the sending process.
 *
 * The file is not opened while this value is created or serialized. For each multipart transmission,
 * [doAttach] creates a fresh [FilePart] whose provider opens [file] lazily through the supplied [Vertx] filesystem.
 * The part uses the path's final component as its filename, defaults to `application/octet-stream`, and owns the
 * resulting asynchronous file handle, so the handle is closed after transmission, failure, or cancellation.
 *
 * The path itself is JSON-serializable, but it remains local state: after deserialization, the process sending the
 * request must be able to access the same path.
 */
data class FileAttachment internal constructor(val file: File) : AbstractFileAttachment() {
    override fun doAttach(field: String, vertx: Vertx): FilePart =
        FilePart(field, file.toPath().fileName.toString(), { vertx.fileSystem().open(file.path, OpenOptions()).coAwait() } )
}
