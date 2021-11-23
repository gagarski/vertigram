package ski.gagar.vxutil.vertigram.entities.requests

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.entities.MaskPosition
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.binaryFileUploadIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
import java.io.File

data class AddStickerToSet(
    val userId: Long,
    val name: String,
    val pngSticker: String,
    // tgs stickers are not supported with json
    val emojis: String,
    val maskPosition: MaskPosition? = null
) : JsonTgCallable<Boolean>()

data class AddStickerToSetMultipart(
    val userId: Long,
    val name: String,
    val pngSticker: File? = null,
    val tgsSticker: File? = null,
    val emojis: String,
    val maskPosition: MaskPosition? = null
) : MultipartTgCallable<Boolean>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("user_id", userId)
        attributeIfNotNull("name", name)
        binaryFileUploadIfNotNull("png_sticker", pngSticker)
        binaryFileUploadIfNotNull("tgs_sticker", tgsSticker)
        attributeIfNotNull("emojis", emojis)
        jsonAttributeIfNotNull("maskPosition", maskPosition)
    }
}
