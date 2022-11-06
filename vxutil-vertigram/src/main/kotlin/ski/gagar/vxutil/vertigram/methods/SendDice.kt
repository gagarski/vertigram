package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.DiceEmoji
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

@TgMethod
@Throttled
data class SendDice(
    override val chatId: ChatId,
    val emoji: DiceEmoji? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null,
    // Since Telegram Bot Api 6.3
    val messageThreadId: Long? = null
) : JsonTgCallable<Message>(), HasChatId
