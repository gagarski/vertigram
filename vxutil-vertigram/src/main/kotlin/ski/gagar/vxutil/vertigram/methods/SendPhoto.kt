package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.attributeIfTrue
import ski.gagar.vxutil.web.binaryFileUploadIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
import java.io.File


data class SendPhoto(
    val chatId: Long,
    val photo: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()

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
