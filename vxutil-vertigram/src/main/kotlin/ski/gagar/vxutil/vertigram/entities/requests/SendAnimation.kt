package ski.gagar.vxutil.vertigram.entities.requests

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.entities.Message
import ski.gagar.vxutil.vertigram.entities.ParseMode
import ski.gagar.vxutil.vertigram.entities.ReplyMarkup
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue
import ski.gagar.vxutil.web.binaryFileUploadIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
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
