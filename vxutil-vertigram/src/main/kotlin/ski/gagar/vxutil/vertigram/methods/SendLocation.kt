package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

data class SendLocation(
    val chatId: ChatId,
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()
