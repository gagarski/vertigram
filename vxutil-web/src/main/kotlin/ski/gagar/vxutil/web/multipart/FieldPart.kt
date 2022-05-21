package ski.gagar.vxutil.web.multipart

import io.vertx.core.buffer.Buffer
import ski.gagar.vxutil.io.SingletonStream

class FieldPart(name: String, value: Any) : Part<SingletonStream<Buffer>>(false) {
    override val contentDisposition = """form-data; name="$name""""

    private val buf = value.toString().asBuffer()

    override suspend fun dataLength(): Long = buf.length().toLong()

    override suspend fun dataStream(): SingletonStream<Buffer> = SingletonStream(buf)

    override suspend fun close(stream: SingletonStream<Buffer>) {}
}
