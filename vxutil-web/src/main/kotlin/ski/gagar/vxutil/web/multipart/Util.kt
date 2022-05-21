package ski.gagar.vxutil.web.multipart

import io.vertx.core.buffer.Buffer
import ski.gagar.vxutil.io.SingletonStream

internal const val NL = "\r\n"
internal fun String.asBuffer() = Buffer.buffer(this)
internal fun Buffer.asSingletonStream() = SingletonStream(this)
internal fun String.asSingletonStream() = SingletonStream(this.asBuffer())
