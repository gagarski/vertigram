package ski.gagar.vxutil.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import ski.gagar.vxutil.io.ReadStreamWrapper

class ReadStreamPart(
    name: String,
    filename: String,
    private val streamProvider: suspend () -> ReadStream<Buffer>,
    contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
    private val dataLength: Long? = null,
    private val closer: (suspend (stream: ReadStream<Buffer>) -> Unit) = {}
) : Part() {
    override val contentDisposition =
        """form-data; name=$name; filename=$filename"""


    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )

    override suspend fun dataLength(): Long? = dataLength

    override suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer =
        ReadStreamWrapper.of(streamProvider, closer)
}
