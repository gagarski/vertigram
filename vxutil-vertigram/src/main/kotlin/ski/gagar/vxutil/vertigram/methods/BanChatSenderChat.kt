package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import java.time.Instant

@TgMethod
data class BanChatSenderChat(
    val chatId: ChatId,
    val senderChatId: Long
) : JsonTgCallable<Boolean>
