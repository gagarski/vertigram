package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.PreparedInlineMessage
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Stores a message that can be sent by a user of a Mini App. Returns a [PreparedInlineMessage] on success.
 *
 * See Telegram's
 * [savePreparedInlineMessage](https://core.telegram.org/bots/api#savepreparedinlinemessage) documentation.
 */
@TelegramCodegen.Method
data class SavePreparedInlineMessage internal constructor(
    /** Unique identifier of the target user who can use the prepared message. */
    val userId: Long,
    /** Result describing the message to be sent. */
    val result: InlineQuery.Result,
    /** Pass `true` if the message can be sent to private chats with users. */
    val allowUserChats: Boolean = false,
    /** Pass `true` if the message can be sent to private chats with bots. */
    val allowBotChats: Boolean = false,
    /** Pass `true` if the message can be sent to group and supergroup chats. */
    val allowGroupChats: Boolean = false,
    /** Pass `true` if the message can be sent to channel chats. */
    val allowChannelChats: Boolean = false
) : JsonTelegramCallable<PreparedInlineMessage>()
