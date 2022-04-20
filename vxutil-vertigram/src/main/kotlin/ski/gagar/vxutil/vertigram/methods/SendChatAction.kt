package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatAction
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class SendChatAction(
    val chatId: ChatId,
    val action: ChatAction
) : JsonTgCallable<Boolean>()
