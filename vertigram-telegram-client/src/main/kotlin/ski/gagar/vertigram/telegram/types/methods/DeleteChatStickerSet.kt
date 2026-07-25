package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.Chat
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to delete a group sticker set from a supergroup. The bot must be an administrator in the chat for
 * this to work and must have the appropriate administrator rights. Use [Chat.FullInfo.canSetStickerSet], returned by
 * [ski.gagar.vertigram.telegram.methods.getChat], to check if the bot can use this method. Returns `true` on success.
 *
 * See Telegram's [deleteChatStickerSet](https://core.telegram.org/bots/api#deletechatstickerset) documentation.
 */
@TelegramCodegen.Method
data class DeleteChatStickerSet internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
