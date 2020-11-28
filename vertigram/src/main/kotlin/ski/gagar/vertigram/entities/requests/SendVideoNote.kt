package ski.gagar.vertigram.entities.requests

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.entities.Message
import ski.gagar.vertigram.entities.ParseMode
import ski.gagar.vertigram.entities.ReplyMarkup
import ski.gagar.vxutil.attributeIfNotNull
import ski.gagar.vxutil.attributeIfTrue
import ski.gagar.vxutil.binaryFileUploadIfNotNull
import ski.gagar.vxutil.jsonAttributeIfNotNull
import java.io.File

data class SendVideoNote(
    val chatId: Long,
    val videoNote: String,
    val duration: Int? = null,
    val length: Int? = null,
    val thumb: String? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()

data class SendVideoNoteMultipart(
    val chatId: Long,
    val videoNote: File,
    val duration: Int? = null,
    val length: Int? = null,
    val thumb: File? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        binaryFileUploadIfNotNull("video_note", videoNote)
        attributeIfNotNull("duration", duration)
        attributeIfNotNull("length", length)
        binaryFileUploadIfNotNull("thumb", thumb)
        attributeIfTrue("disable_notification", disableNotification)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
    }
}