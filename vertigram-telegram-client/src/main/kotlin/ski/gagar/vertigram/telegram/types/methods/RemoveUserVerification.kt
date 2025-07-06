package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [removeUserVerification](https://core.telegram.org/bots/api#removeuserverification) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class RemoveUserVerification internal constructor(
    val userId: Long
) : JsonTelegramCallable<Boolean>()
