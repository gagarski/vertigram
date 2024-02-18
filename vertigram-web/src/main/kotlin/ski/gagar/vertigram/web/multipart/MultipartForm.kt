package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.apache.commons.lang3.RandomStringUtils
import ski.gagar.vertigram.io.ConcatStream
import ski.gagar.vertigram.io.ReadStreamWrapper

class MultipartForm(val parts: List<Part>) {
    private val boundary =
        RandomStringUtils.random(69, BOUNDARY_CHARS) + RandomStringUtils.random(1, BOUNDARY_LAST_CHARS)
    private val boundaryLine = "--$boundary$NL"
    private val boundaryLineLast = "--$boundary--$NL"

    constructor(vararg items: Part) : this(items.toList())

    private suspend fun contentLength(): Long? {
        if (parts.any { it.length() == null }) {
            return null
        }

        var length = 0L
        for (item in parts) {
            val len = item.length() ?: return null

            length += boundaryLine.length
            length += len
        }
        length += boundaryLineLast.length
        return length
    }

    private suspend fun stream(scope: CoroutineScope): ReadStream<Buffer> =
        scope.ConcatStream(mutableListOf<ReadStreamWrapperBuffer>().apply {
            for (item in parts) {
                add(ReadStreamWrapper.ofNonCloseable(boundaryLine.asSingletonStream()))
                add(ReadStreamWrapper.ofNonCloseable(item.stream(scope)))
            }
            add(ReadStreamWrapper.ofNonCloseable(boundaryLineLast.asSingletonStream()))
        })


    suspend fun send(req: HttpRequest<Buffer>): HttpResponse<Buffer> = coroutineScope {
        req.apply {
            putHeader(
                "${HttpHeaderNames.CONTENT_TYPE}",
                "${HttpHeaderValues.MULTIPART_FORM_DATA}; boundary=$boundary"
            )
            contentLength()?.let {
                putHeader("${HttpHeaderNames.CONTENT_LENGTH}", "$it")
            }

        }
        return@coroutineScope req.sendStream(stream(this@coroutineScope)).coAwait()
    }



    companion object {
        const val BOUNDARY_LAST_CHARS =
            "0123456789" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "'()+_,-./:=?"
        const val BOUNDARY_CHARS = "$BOUNDARY_LAST_CHARS "
    }
}
