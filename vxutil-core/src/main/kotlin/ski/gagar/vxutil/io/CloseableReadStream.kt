package ski.gagar.vxutil.io

import io.vertx.core.streams.ReadStream

interface CloseableReadStream<T> : ReadStream<T> {
    suspend fun close()
}
