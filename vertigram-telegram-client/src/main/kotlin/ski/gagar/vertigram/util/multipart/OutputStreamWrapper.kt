package ski.gagar.vertigram.util.multipart

import io.vertx.core.Vertx
import ski.gagar.vertigram.web.multipart.Part
import java.io.OutputStream

/**
 * Wrapper that supports [defer] method, which allows [ski.gagar.vertigram.util.json.AttachmentSerializer] to
 * put attachments into it. The serializer is aware of this OutputStream implementation.
 */
class OutputStreamWrapper(
    val delegate: OutputStream,
    val vertx: Vertx,
) : OutputStream() {
    val deferredAttachments_: MutableMap<String, Part> = mutableMapOf()

    override fun write(b: Int) {
        delegate.write(b)
    }

    override fun write(b: ByteArray?) {
        delegate.write(b)
    }

    override fun write(b: ByteArray?, off: Int, len: Int) {
        delegate.write(b, off, len)
    }

    override fun flush() {
        delegate.flush()
    }

    override fun close() {
        delegate.close()
    }

    fun defer(name: String, part: Part) {
        deferredAttachments_[name] = part
    }

    val deferredAttachments: Map<String, Part>
        get() = deferredAttachments_
}