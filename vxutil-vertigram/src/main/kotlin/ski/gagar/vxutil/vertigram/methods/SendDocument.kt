package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull

@TgMethod
data class SendDocument(
    val chatId: ChatId,
    val document: Attachment,
    val thumb: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableContentTypeDetection: Boolean = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message> {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        attachDirectly("document", document)
        attachDirectly("thumb", document)
        attributeIfNotNull("caption", caption)
        attributeIfNotNull("parse_mode", parseMode)
        jsonAttributeIfNotNull("caption_entities", captionEntities)
        jsonAttributeIfNotNull("disable_content_type_detection", disableContentTypeDetection)
        attributeIfNotNull("disable_notification", disableNotification)
        attributeIfNotNull("protect_content", protectContent)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        attributeIfNotNull("allow_sending_without_reply", allowSendingWithoutReply)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
    }
}
