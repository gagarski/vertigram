package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue
import ski.gagar.vxutil.web.binaryFileUploadIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
import java.io.File
import java.time.Duration

@TgMethod
data class SendVoice(
    val chatId: ChatId,
    val voice: Attachment,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntitites: List<MessageEntity>? = null,
    val duration: Duration? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message> {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        attachDirectly("voice", voice)
        attributeIfNotNull("caption", caption)
        attributeIfNotNull("parse_mode", parseMode)
        jsonAttributeIfNotNull("caption_entities", captionEntitites)
        attributeIfNotNull("duration", duration?.toSeconds())
        attributeIfTrue("disable_notification", disableNotification)
        attributeIfTrue("protect_content", protectContent)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        attributeIfTrue("allow_sending_without_reply", allowSendingWithoutReply)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
    }
}
