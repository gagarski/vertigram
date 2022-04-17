package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class PinChatMessage(
    val chatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false
) : JsonTgCallable<Boolean>
