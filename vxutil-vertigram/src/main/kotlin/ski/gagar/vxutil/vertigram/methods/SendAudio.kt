package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue
import ski.gagar.vxutil.web.jsonAttributeIfNotNull

data class SendAudio(
    val chatId: ChatId,
    val audio: Attachment,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val duration: Long? = null,
    val performer: String? = null,
    val title: String? = null,
    val thumb: Attachment? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        attachDirectly("audio", audio)
        attributeIfNotNull("caption", caption)
        attributeIfNotNull("parse_mode", parseMode)
        jsonAttributeIfNotNull("caption_entities", captionEntities, TELEGRAM_JSON_MAPPER)
        attributeIfNotNull("duration", duration)
        attributeIfNotNull("performer", performer)
        attributeIfNotNull("title", title)
        attachDirectly("thumb", thumb)
        attributeIfTrue("disable_notification", disableNotification)
        attributeIfTrue("protect_content", protectContent)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        attributeIfTrue("allow_sending_without_reply", allowSendingWithoutReply)
        jsonAttributeIfNotNull("reply_markup", replyMarkup, TELEGRAM_JSON_MAPPER)
    }
}
