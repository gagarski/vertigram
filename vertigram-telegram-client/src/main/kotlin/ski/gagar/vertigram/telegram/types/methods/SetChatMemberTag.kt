package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to set a tag for a regular member in a group or supergroup. Returns `true` on success.
 *
 * See Telegram's [setChatMemberTag](https://core.telegram.org/bots/api#setchatmembertag) documentation.
 */
@TelegramCodegen.Method
data class SetChatMemberTag internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long,
    /** New tag for the member; 0-16 characters, emoji aren't allowed. */
    val tag: String? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
