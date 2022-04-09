package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.types.attachments.attachIndirectly
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.InputMedia
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull

data class EditMessageMedia(
    val media: InputMedia,
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null
) : MultipartTgCallable<Message>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        val toAttach = putMediaDescriptor()
        attributeIfNotNull("chat_id", chatId)
        attributeIfNotNull("message_id", messageId)
        attributeIfNotNull("inline_message_id", inlineMessageId)
        jsonAttributeIfNotNull("reply_markup", replyMarkup)
        attachMedia()
    }

    private fun MultipartForm.putMediaDescriptor() {
        jsonAttributeIfNotNull("media", media.instantiate(
            media = media.media.getIndirectAttachment(ATTACHMENT_MEDIA),
            thumb = media.thumb?.getIndirectAttachment(ATTACHMENT_THUMB)
        ), TELEGRAM_JSON_MAPPER)
    }

    private fun MultipartForm.attachMedia() {
        attachIndirectly(ATTACHMENT_MEDIA, media.media)
        media.thumb?.let {
            attachIndirectly(ATTACHMENT_THUMB, it)
        }
    }

    companion object {
        const val ATTACHMENT_MEDIA = "attachment_media"
        const val ATTACHMENT_THUMB = "attachment_thumb"
    }
}
