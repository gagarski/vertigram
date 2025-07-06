package ski.gagar.vertigram.util.multipart

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.Vertx

/**
 * Mapper with to-plevel multipart support
 */
internal fun telegramJsonMapperWithMultipart(underlying: ObjectMapper, vertx: Vertx) =
    MultipartMapper(underlying, vertx)
