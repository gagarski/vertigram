package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to set a new group sticker set for a supergroup. Returns `true` on success.
 *
 * See Telegram's [setChatStickerSet](https://core.telegram.org/bots/api#setchatstickerset) documentation.
 */
@TelegramCodegen.Method
data class SetChatStickerSet internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Name of the sticker set to set as the group sticker set. */
    val stickerSetName: String
) : JsonTelegramCallable<Boolean>(), HasChatId
