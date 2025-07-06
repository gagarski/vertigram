package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [verifyUser](https://core.telegram.org/bots/api#verifyuser) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class VerifyUser internal constructor(
    val userId: Long,
    val customDescription: String? = null
) : JsonTelegramCallable<Boolean>()
