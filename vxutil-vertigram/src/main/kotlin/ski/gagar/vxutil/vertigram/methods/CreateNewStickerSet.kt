package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.attachDirectly
import ski.gagar.vxutil.vertigram.types.MaskPosition
import ski.gagar.vxutil.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.binaryFileUploadIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
import java.io.File

data class CreateNewStickerSet(
    val userId: Long,
    val name: String,
    val title: String,
    val pngSticker: Attachment? = null,
    val tgsSticker: Attachment? = null,
    val webmSticker: Attachment? = null,
    val emojis: String,
    val containsMasks: Boolean? = null,
    val maskPosition: MaskPosition? = null
) : MultipartTgCallable<Boolean>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("user_id", userId)
        attributeIfNotNull("name", name)
        attributeIfNotNull("title", title)
        attachDirectly("png_sticker", pngSticker)
        attachDirectly("tgs_sticker", tgsSticker)
        attachDirectly("webm_sticker", webmSticker)
        attributeIfNotNull("emojis", emojis)
        attributeIfNotNull("contains_masks", containsMasks)
        jsonAttributeIfNotNull("mask_position", maskPosition, TELEGRAM_JSON_MAPPER)
    }
}
