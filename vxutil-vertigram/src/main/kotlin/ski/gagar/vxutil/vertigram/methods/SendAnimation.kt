package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue
import ski.gagar.vxutil.web.jsonAttributeIfNotNull

data class SendAnimation(
    val chatId: ChatId,
    val animation: Attachment,
    val duration: Long? = null,
    val width: Long? = null,
    val height: Long? = null,
    val thumb: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        attachDirectly("animation", animation)
        attributeIfNotNull("duration", duration)
        attributeIfNotNull("width", width)
        attributeIfNotNull("height", height)
        attachDirectly("thumb", thumb)
        attributeIfNotNull("caption", caption)
        attributeIfNotNull("parse_mode", parseMode)
        jsonAttributeIfNotNull("caption_entities", captionEntities, TELEGRAM_JSON_MAPPER)
        attributeIfTrue("disable_notification", disableNotification)
        attributeIfTrue("protect_content", protectContent)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        attributeIfTrue("allow_sending_without_reply", allowSendingWithoutReply)
        jsonAttributeIfNotNull("reply_markup", replyMarkup, TELEGRAM_JSON_MAPPER)
    }
}
