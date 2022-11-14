package ski.gagar.vxutil.vertigram.util

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.vertx.core.http.impl.headers.HeadersMultiMap
import ski.gagar.vxutil.jackson.BadRequest
import ski.gagar.vxutil.toMultiMap
import ski.gagar.vxutil.vertigram.methods.TgCallable


@JsonIgnoreProperties("message")
abstract class TelegramException(str: String) : Exception(str)

@JsonIgnoreProperties("message")
abstract class TelegramCallException(
    val status: Int,
    val ok: Boolean,
    val description: String?,
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    val call: TgCallable<*>,
    val responseHeaders: Map<String, List<String>>
) : TelegramException("Telegram call $call returned ${status}: $description") {

    @get:JsonIgnore
    val responseHeadersMultiMap by lazy {
        responseHeaders.toMultiMap { HeadersMultiMap() }
    }

    companion object {
        fun create(
            status: Int,
            ok: Boolean,
            description: String?,
            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
            call: TgCallable<*>,
            responseHeaders: Map<String, List<String>>
        ) = when (status) {
            in 400..499 -> TelegramCallClientException(status, ok, description, call, responseHeaders)
            else -> TelegramCallServerException(status, ok, description, call, responseHeaders)
        }
    }

}

@JsonIgnoreProperties("message")
class TelegramCallClientException(
    status: Int,
    ok: Boolean,
    description: String?,
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    call: TgCallable<*>,
    responseHeaders: Map<String, List<String>>
) : TelegramCallException(status, ok, description, call, responseHeaders), BadRequest

@JsonIgnoreProperties("message")
class TelegramCallServerException(
    status: Int,
    ok: Boolean,
    description: String?,
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    call: TgCallable<*>,
    responseHeaders: Map<String, List<String>>
) : TelegramCallException(status, ok, description, call, responseHeaders)

@JsonIgnoreProperties("message")
abstract class TelegramDownloadException(val status: Int, val path: String) : TelegramException("Failed to download file: $status") {
    companion object {
        fun create(
            status: Int, path: String
        ) = when (status) {
            in 400..499 -> TelegramDownloadClientException(status, path)
            else -> TelegramDownloadServerException(status, path)
        }
    }
}

class TelegramDownloadClientException(status: Int, path: String) : TelegramDownloadException(status, path), BadRequest
class TelegramDownloadServerException(status: Int, path: String) : TelegramDownloadException(status, path)

@JsonIgnoreProperties("message")
data class TelegramNoFilePathException(val id: String) : TelegramException("getFile did not return a file path for $id"), BadRequest
