package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

@TgMethod
data class EditMessageLiveLocation(
    val latitude: Double,
    val longitude: Double,
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null,
    val horizontalAccuracy: Double? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>
