package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.Reaction
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the chosen reactions on a message. Returns `true` on success.
 *
 * See Telegram's [setMessageReaction](https://core.telegram.org/bots/api#setmessagereaction) documentation.
 */
@TelegramCodegen.Method
data class SetMessageReaction internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    val chatId: ChatId,
    /** Identifier of the target message. */
    val messageId: Long,
    /** New reactions set on the message by the bot. */
    val reaction: List<Reaction>? = null,
    /** Pass `true` to set the reaction with a big animation. */
    @get:JvmName("getIsBig")
    val isBig: Boolean = false
) : JsonTelegramCallable<Boolean>()
