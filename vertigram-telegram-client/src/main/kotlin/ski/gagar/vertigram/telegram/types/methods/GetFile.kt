package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getFile](https://core.telegram.org/bots/api#getfile) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class GetFile(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val fileId: String
) : JsonTelegramCallable<File>()
