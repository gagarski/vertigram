package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [deleteChatStickerSet](https://core.telegram.org/bots/api#deletechatstickerset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeleteChatStickerSet internal constructor(
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
