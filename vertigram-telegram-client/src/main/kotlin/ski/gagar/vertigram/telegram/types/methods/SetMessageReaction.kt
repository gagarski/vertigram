package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.Reaction
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMessageReaction](https://core.telegram.org/bots/api#setmessagereaction) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class SetMessageReaction(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val chatId: ChatId,
    val messageId: Long,
    val reaction: List<Reaction>? = null,
    @get:JvmName("getIsBig")
    val isBig: Boolean = false
) : JsonTelegramCallable<Boolean>()
