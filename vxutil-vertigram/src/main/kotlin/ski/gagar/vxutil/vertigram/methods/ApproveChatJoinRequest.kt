package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class ApproveChatJoinRequest(
    val chatId: ChatId,
    val userId: Long
) : JsonTgCallable<Boolean>()