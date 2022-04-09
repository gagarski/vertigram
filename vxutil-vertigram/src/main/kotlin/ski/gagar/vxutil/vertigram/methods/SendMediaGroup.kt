package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.vertigram.types.attachments.attachIndirectly
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InputMedia
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull

data class SendMediaGroup(
    val chatId: ChatId,
    val media: List<InputMedia>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
) : MultipartTgCallable<List<Message>>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("chat_id", chatId)
        val toAttach = putMediaDescriptor()
        attributeIfNotNull("disable_notification", disableNotification)
        attributeIfNotNull("protect_content", protectContent)
        attributeIfNotNull("reply_to_message_id", replyToMessageId)
        jsonAttributeIfNotNull("allow_sending_without_reply", allowSendingWithoutReply)
        attachMedia(toAttach)
    }

    private fun MultipartForm.putMediaDescriptor(): List<ProcessedMedia> {
        val processed = media.asSequence().withIndex().map { (index, it) ->
            ProcessedMedia(index, it, it.instantiate(
                media = it.media.getIndirectAttachment("$ATTACHMENT_MEDIA_PREFIX$index"),
                thumb = it.thumb?.getIndirectAttachment("$ATTACHMENT_THUMB_PREFIX$index")
            ))
        }.toList()

        jsonAttributeIfNotNull("media", processed.map { it.processed }, TELEGRAM_JSON_MAPPER)

        return processed
    }

    private fun MultipartForm.attachMedia(media: List<ProcessedMedia>) {
        for (medium in media) {
            attachIndirectly("$ATTACHMENT_MEDIA_PREFIX${medium.index}", medium.original.media)
            medium.original.thumb?.let {
                attachIndirectly("$ATTACHMENT_THUMB_PREFIX${medium.index}", it)
            }
        }
    }

    private data class ProcessedMedia(val index: Int, val original: InputMedia, val processed: InputMedia)

    companion object {
        const val ATTACHMENT_MEDIA_PREFIX = "attachment_media_"
        const val ATTACHMENT_THUMB_PREFIX = "attachment_thumb_"
    }
}
