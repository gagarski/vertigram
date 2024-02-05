package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ReactionType

@TgMethod
data class SetMessageReaction(
    val chatId: ChatId,
    val messageId: Long,
    val reaction: List<ReactionType> = listOf(),
    @get:JvmName("getIsBig")
    val isBig: Boolean = false
) : JsonTgCallable<Boolean>()
