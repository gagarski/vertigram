package ski.gagar.vxutil.vertigram.util.multipart

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.Vertx

internal fun telegramJsonMapperWithMultipart(underlying: ObjectMapper, vertx: Vertx) =
    ObjectMapperWithMultipart(underlying, vertx)
