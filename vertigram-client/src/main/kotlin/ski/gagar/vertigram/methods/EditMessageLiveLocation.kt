package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyMarkup

@Throttled
data class EditMessageLiveLocation(
    val latitude: Double,
    val longitude: Double,
    override val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null,
    val horizontalAccuracy: Double? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId