package ski.gagar.vxutil.vertigram.util

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ski.gagar.vxutil.jackson.DoNotSuppressError


@JsonIgnoreProperties("message")
abstract class TelegramException(str: String) : Exception(str), DoNotSuppressError

@JsonIgnoreProperties("message")
data class TelegramCallException(val status: Int, val ok: Boolean, val description: String?) :
    TelegramException("Telegram call returned ${status}: $description")

@JsonIgnoreProperties("message")
data class TelegramDownloadException(val status: Int) : TelegramException("Failed to download file: $status")

@JsonIgnoreProperties("message")
object TelegramNoFilePathException : TelegramException("getFile did not return a file path")
