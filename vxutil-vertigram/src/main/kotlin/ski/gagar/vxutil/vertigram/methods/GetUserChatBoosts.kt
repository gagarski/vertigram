package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.UserChatBoosts

@TgMethod
data class GetUserChatBoosts(
    val chatId: ChatId,
    val userId: Long
) : JsonTgCallable<UserChatBoosts>()
