package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setChatStickerSet](https://core.telegram.org/bots/api#setchatstickerset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetChatStickerSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val stickerSetName: String
) : JsonTelegramCallable<Boolean>(), HasChatId
