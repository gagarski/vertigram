package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ChatInviteLink
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
