package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File

/**
 * Use this method to get basic information about a file and prepare it for downloading.
 *
 * The file can then be downloaded via `https://api.telegram.org/file/bot<token>/<file_path>`. A link is guaranteed to
 * be valid for at least 1 hour. When the link expires, a new one can be requested by calling
 * [ski.gagar.vertigram.telegram.client.Telegram.getFile] again.
 *
 * See Telegram's [getFile](https://core.telegram.org/bots/api#getfile) documentation.
 */
@TelegramCodegen.Method
data class GetFile internal constructor(
    /** Identifier of the file to get. */
    val fileId: String
) : JsonTelegramCallable<File>()
