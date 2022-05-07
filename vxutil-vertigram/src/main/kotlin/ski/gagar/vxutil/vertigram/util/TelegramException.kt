package ski.gagar.vxutil.vertigram.util

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vxutil.jackson.BadRequest
import ski.gagar.vxutil.vertigram.methods.TgCallable


@JsonIgnoreProperties("message")
abstract class TelegramException(str: String) : Exception(str)

@JsonIgnoreProperties("message")
abstract class TelegramCallException(
    val status: Int,
    val ok: Boolean,
    val description: String?,
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    val call: TgCallable<*>
) : TelegramException("Telegram call $call returned ${status}: $description") {
    companion object {
        fun create(
            status: Int,
            ok: Boolean,
            description: String?,
            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
            call: TgCallable<*>
        ) = when (status) {
            in 400..499 -> TelegramCallClientException(status, ok, description, call)
            else -> TelegramCallServerException(status, ok, description, call)
        }
    }

}

@JsonIgnoreProperties("message")
class TelegramCallClientException(
    status: Int,
    ok: Boolean,
    description: String?,
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    call: TgCallable<*>) : TelegramCallException(status, ok, description, call), BadRequest

@JsonIgnoreProperties("message")
class TelegramCallServerException(
    status: Int,
    ok: Boolean,
    description: String?,
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    call: TgCallable<*>) : TelegramCallException(status, ok, description, call)

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
