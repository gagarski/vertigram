package ski.gagar.vertigram.entities.requests

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.entities.Message
import ski.gagar.vertigram.entities.ParseMode
import ski.gagar.vertigram.entities.ReplyMarkup
import ski.gagar.vxutil.attributeIfNotNull
import ski.gagar.vxutil.binaryFileUploadIfNotNull
import ski.gagar.vxutil.jsonAttributeIfNotNull
import java.io.File


data class SendDocument(
    val chatId: Long,
    val document: String,
    val thumb: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null
) : JsonTgCallable<Message>()

data class SendDocumentMultipart(
    val chatId: Long,
    val document: File,
    val thumb: File? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTgCallable<Message>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        binaryFileUploadIfNotNull("document", document)
        attributeIfNotNull("parse_mode", parseMode)
        attributeIfNotNull("disable_notification", disableNotification)
        binaryFileUploadIfNotNull("thumb", thumb)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        attributeIfNotNull("caption", caption)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
    }
}

