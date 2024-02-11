package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatInviteLink
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [revokeChatInviteLink](https://core.telegram.org/bots/api#revokechatinvitelink) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class RevokeChatInviteLink(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val inviteLink: String
) : JsonTelegramCallable<ChatInviteLink>(), HasChatId
