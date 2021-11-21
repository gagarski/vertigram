package ski.gagar.vxutil

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.kotlin.coroutines.awaitResult

suspend fun <T, U> HttpRequest<T>.sendJsonAwait(obj: U, mapper: ObjectMapper) =
    awaitResult<HttpResponse<T>> { sendJsonObject(
        jsonObjectMapFrom(
            obj,
            mapper
        ), it) }
