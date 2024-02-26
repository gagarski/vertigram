package ski.gagar.vertigram.web.multipart

import io.vertx.core.buffer.Buffer
import ski.gagar.vertigram.io.SingletonStream

internal const val NL = "\r\n"
internal fun String.asBuffer() = Buffer.buffer(this)
internal fun Buffer.asSingletonStream() = SingletonStream(this)
internal fun String.asSingletonStream() = SingletonStream(this.asBuffer())
