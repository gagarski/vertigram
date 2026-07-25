package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Removes verification from a user who is currently verified on behalf of the organization represented by the bot.
 * Returns `true` on success.
 *
 * See Telegram's [removeUserVerification](https://core.telegram.org/bots/api#removeuserverification) documentation.
 */
@TelegramCodegen.Method
data class RemoveUserVerification internal constructor(
    /** Unique identifier of the target user. */
    val userId: Long
) : JsonTelegramCallable<Boolean>()
