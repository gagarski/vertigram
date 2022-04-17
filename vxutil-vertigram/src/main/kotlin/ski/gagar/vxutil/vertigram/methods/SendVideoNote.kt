package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
import java.time.Duration

@TgMethod
data class SendVideoNote(
    val chatId: ChatId,
    val videoNote: Attachment,
    val duration: Duration? = null,
    val length: Int? = null,
    val thumb: Attachment? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message> {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        attachDirectly("video_note", videoNote)
        attributeIfNotNull("duration", duration?.toSeconds())
        attributeIfNotNull("length", length)
        attachDirectly("thumb", thumb)
        attributeIfTrue("disable_notification", disableNotification)
        attributeIfTrue("protect_content", protectContent)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        attributeIfTrue("allow_sending_without_reply", allowSendingWithoutReply)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
    }
}
