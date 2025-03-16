package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [ChatJoinRequest](https://core.telegram.org/bots/api#chatjoinrequest) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ChatJoinRequest internal constructor(
    val chat: Chat,
    val from: User,
    val userChatId: Long,
    val date: Instant,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null
) {
    companion object
}