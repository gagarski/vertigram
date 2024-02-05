package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

@TgMethod
data class DeleteChatStickerSet(
    override val chatId: ChatId
) : JsonTgCallable<Boolean>(), HasChatId
