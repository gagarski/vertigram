package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Represents a join request sent to a chat.
 *
 * See Telegram's [ChatJoinRequest](https://core.telegram.org/bots/api#chatjoinrequest) documentation.
 */
@TelegramCodegen.Type
data class ChatJoinRequest internal constructor(
    /** Chat to which the request was sent. */
    val chat: Chat,
    /** User that sent the join request. */
    val from: User,
    /**
     * Identifier of a private chat with the user who sent the join request. The bot can use this identifier for 5
     * minutes to send messages until the join request is processed, assuming no other administrator contacted the
     * user.
     */
    val userChatId: Long,
    /** Date the request was sent. */
    val date: Instant,
    /**
     * Identifier of the join request query; for bots assigned to process join requests only. If present, the bot must
     * call [sendChatJoinRequestWebApp][ski.gagar.vertigram.telegram.methods.sendChatJoinRequestWebApp] or
     * [answerChatJoinRequestQuery][ski.gagar.vertigram.telegram.methods.answerChatJoinRequestQuery] within 10 seconds.
     */
    val queryId: String? = null,
    /** Bio of the user. */
    val bio: String? = null,
    /** Chat invite link that was used by the user to send the join request. */
    val inviteLink: ChatInviteLink? = null
) {
    companion object
}
