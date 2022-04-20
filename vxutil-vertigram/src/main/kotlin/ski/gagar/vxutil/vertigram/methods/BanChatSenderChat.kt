package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class BanChatSenderChat(
    val chatId: ChatId,
    val senderChatId: Long
) : JsonTgCallable<Boolean>()