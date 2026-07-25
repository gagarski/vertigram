package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Verifies a chat on behalf of the organization represented by the bot. Returns `true` on success.
 *
 * See Telegram's [verifyChat](https://core.telegram.org/bots/api#verifychat) documentation.
 */
@TelegramCodegen.Method
data class VerifyChat internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    val chatId: ChatId,
    /** Custom description for the verification; 0-70 characters. */
    val customDescription: String? = null
) : JsonTelegramCallable<Boolean>()
