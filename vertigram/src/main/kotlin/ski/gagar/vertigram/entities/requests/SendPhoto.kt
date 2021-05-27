package ski.gagar.vertigram.entities.requests

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.entities.Message
import ski.gagar.vertigram.entities.ParseMode
import ski.gagar.vertigram.entities.ReplyMarkup
import ski.gagar.vxutil.attributeIfNotNull
import ski.gagar.vxutil.attributeIfTrue
import ski.gagar.vxutil.binaryFileUploadIfNotNull
import ski.gagar.vxutil.jsonAttributeIfNotNull
import java.io.File

@TgMethod
data class SendPhoto(
    val chatId: Long,
    val photo: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()

@TgMethod(type = TgMethod.MULTIPART)
data class SendPhotoMultipart(
    val chatId: Long,
    val photo: File,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        binaryFileUploadIfNotNull("photo", photo)
        attributeIfNotNull("parse_mode", parseMode)
        attributeIfTrue("disable_notification", disableNotification)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        attributeIfNotNull("caption", caption)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
    }
}
