package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [ChatMemberUpdated](https://core.telegram.org/bots/api#chatmemberupdated) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ChatMemberUpdated(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val chat: Chat,
    val from: User,
    val date: Instant,
    val oldChatMember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null,
    val viaChatFolderInviteLink: Boolean = false
)
