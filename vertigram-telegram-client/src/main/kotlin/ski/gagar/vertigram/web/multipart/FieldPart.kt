package ski.gagar.vertigram.web.multipart

import ski.gagar.vertigram.util.io.ReadStreamWrapper
import ski.gagar.vertigram.util.io.SingletonStream

class FieldPart(name: String, value: Any) : Part() {
    override val contentDisposition = """form-data; name="$name""""

    private val buf = value.toString().asBuffer()

    override suspend fun dataLength(): Long = buf.length().toLong()

    override suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer =
        ReadStreamWrapper.ofNonCloseable(SingletonStream(buf))
}
