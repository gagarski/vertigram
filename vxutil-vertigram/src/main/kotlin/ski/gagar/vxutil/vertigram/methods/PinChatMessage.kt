package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class PinChatMessage(
    val chatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false
) : JsonTgCallable<Boolean>()
