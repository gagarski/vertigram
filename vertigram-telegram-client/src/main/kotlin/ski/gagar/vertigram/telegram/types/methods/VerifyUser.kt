package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Verifies a user on behalf of the organization represented by the bot. Returns `true` on success.
 *
 * See Telegram's [verifyUser](https://core.telegram.org/bots/api#verifyuser) documentation.
 */
@TelegramCodegen.Method
data class VerifyUser internal constructor(
    /** Unique identifier of the target user. */
    val userId: Long,
    /** Custom description for the verification; 0-70 characters. */
    val customDescription: String? = null
) : JsonTelegramCallable<Boolean>()
