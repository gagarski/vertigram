package ski.gagar.vxutil.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import kotlinx.coroutines.coroutineScope
import ski.gagar.vxutil.io.ConcatStream

abstract class Part<T : ReadStream<Buffer>>(private val streamOwned: Boolean) {
    abstract val contentDisposition: String
    open val headers = linkedMapOf<String, String>()
    private var stream: T? = null

    private val headersBuffer: Buffer by lazy {
        val buf = Buffer.buffer()
        buf.appendString("${HttpHeaderNames.CONTENT_DISPOSITION}: $contentDisposition$NL")
        for ((k, v) in headers) {
            buf.appendString("$k: $v$NL")
        }
        buf.appendString(NL)
    }

    private fun headersLength() = headersBuffer.length().toLong()

    protected abstract suspend fun dataStream(): T

    private suspend fun getAndAcquireDataStream(): T {
        val stream = dataStream()
        this.stream = stream
        return stream
    }

    protected open suspend fun dataLength(): Long? = null

    private fun trailingLength() = NL.length.toLong()

    suspend fun stream(): ReadStream<Buffer> = coroutineScope {
        ConcatStream(
            headersBuffer.asSingletonStream(),
            getAndAcquireDataStream(),
            NL.asSingletonStream(),
        )
    }

    protected abstract suspend fun close(stream: T)

    suspend fun closeIfNeeded() {
        val stream = this.stream
        if (streamOwned && null != stream) {
            close(stream)
        }
    }

    suspend fun length() = dataLength()?.let {
        headersLength() + it + trailingLength()
    }

    companion object
}
