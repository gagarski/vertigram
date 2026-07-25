package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Removes verification from a chat that is currently verified on behalf of the organization represented by the bot.
 * Returns `true` on success.
 *
 * See Telegram's [removeChatVerification](https://core.telegram.org/bots/api#removechatverification) documentation.
 */
@TelegramCodegen.Method
data class RemoveChatVerification internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    val chatId: ChatId
) : JsonTelegramCallable<Boolean>()
