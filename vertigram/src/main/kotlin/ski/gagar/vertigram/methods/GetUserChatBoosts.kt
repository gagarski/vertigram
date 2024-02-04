package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.UserChatBoosts

@TgMethod
data class GetUserChatBoosts(
    val chatId: ChatId,
    val userId: Long
) : JsonTgCallable<UserChatBoosts>()
