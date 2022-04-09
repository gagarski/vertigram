package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.Chat
import ski.gagar.vxutil.vertigram.types.ChatId

data class DeleteChatStickerSet(
    val chatId: ChatId
) : JsonTgCallable<Boolean>()
