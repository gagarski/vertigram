package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [ChatJoinRequest](https://core.telegram.org/bots/api#chatjoinrequest) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ChatJoinRequest(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val chat: Chat,
    val from: User,
    val userChatId: Long,
    val date: Instant,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null
)
