package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.attachments.attachIndirectly
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InputMedia
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.vertigram.util.TgMedia
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull

@TgMethod
data class SendMediaGroup(
    val chatId: ChatId,
    @TgMedia
    val media: List<InputMedia>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
) : MultipartTgCallable<List<Message>> {
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
                media = it.media.getIndirectAttachment("$MEDIA$index"),
                thumb = it.thumb?.getIndirectAttachment("$THUMB$index")
            ))
        }.toList()

        jsonAttributeIfNotNull("media", processed.map { it.processed }, TELEGRAM_JSON_MAPPER)

        return processed
    }

    private fun MultipartForm.attachMedia(media: List<ProcessedMedia>) {
        for (medium in media) {
            attachIndirectly("$MEDIA${medium.index}", medium.original.media)
            medium.original.thumb?.let {
                attachIndirectly("$THUMB${medium.index}", it)
            }
        }
    }

    private data class ProcessedMedia(val index: Int, val original: InputMedia, val processed: InputMedia)

    companion object {
        const val ATTACHMENT = "attachment"
        const val MEDIA = "media"
        const val THUMB = "thumb"
    }
}
