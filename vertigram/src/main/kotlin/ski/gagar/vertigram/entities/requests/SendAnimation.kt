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

data class SendAnimation(
    val chatId: Long,
    val animation: String,
    val duration: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
    val thumb: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()

data class SendAnimationMultipart(
    val chatId: Long,
    val animation: File,
    val duration: Int? = null,
    val width: Int? = null,
    val height: Int? = null,
    val thumb: File? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        binaryFileUploadIfNotNull("animation", animation)
        attributeIfNotNull("duration", duration)
        attributeIfNotNull("width", width)
        attributeIfNotNull("height", height)
        binaryFileUploadIfNotNull("thumb", thumb)
        attributeIfNotNull("caption", caption)
        attributeIfNotNull("parse_mode", parseMode)
        attributeIfTrue("disable_notification", disableNotification)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
    }
}