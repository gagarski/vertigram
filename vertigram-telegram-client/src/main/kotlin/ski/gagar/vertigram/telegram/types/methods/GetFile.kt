package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File

/**
 * Telegram [getFile](https://core.telegram.org/bots/api#getfile) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetFile internal constructor(
    val fileId: String
) : JsonTelegramCallable<File>()
