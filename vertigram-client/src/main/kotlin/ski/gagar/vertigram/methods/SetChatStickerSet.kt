package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

data class SetChatStickerSet(
    override val chatId: ChatId,
    val stickerSetName: String
) : JsonTelegramCallable<Boolean>(), HasChatId
