package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vertigram.types.Message

@Throttled
data class EditMessageReplyMarkup(
    override val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId
