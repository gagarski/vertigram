package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

data class EditMessageLiveLocation(
    val latitude: Double,
    val longitude: Double,
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null,
    val horizontalAccuracy: Double? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()
