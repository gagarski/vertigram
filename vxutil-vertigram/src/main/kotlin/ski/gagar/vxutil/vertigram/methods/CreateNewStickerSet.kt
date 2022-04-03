package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vxutil.vertigram.types.MaskPosition
import ski.gagar.vxutil.web.attributeIfNotNull
import ski.gagar.vxutil.web.binaryFileUploadIfNotNull
import ski.gagar.vxutil.web.jsonAttributeIfNotNull
import java.io.File

data class CreateNewStickerSet(
    val userId: Long,
    val name: String,
    val title: String,
    val pngSticker: String,
    // tgs stickers are not supported with json
    val emojis: String,
    val containsMasks: Boolean? = null,
    val maskPosition: MaskPosition? = null
) : ski.gagar.vxutil.vertigram.methods.JsonTgCallable<Boolean>()

data class CreateNewStickerSetMultipart(
    val userId: Long,
    val name: String,
    val title: String,
    val pngSticker: File? = null,
    val tgsSticker: File? = null,
    val emojis: String,
    val containsMasks: Boolean? = null,
    val maskPosition: MaskPosition? = null
) : MultipartTgCallable<Boolean>() {
    override fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper) {
        attributeIfNotNull("user_id", userId)
        attributeIfNotNull("name", name)
        attributeIfNotNull("title", title)
        binaryFileUploadIfNotNull("png_sticker", pngSticker)
        binaryFileUploadIfNotNull("tgs_sticker", tgsSticker)
        attributeIfNotNull("emojis", emojis)
        attributeIfNotNull("containsMasks", containsMasks)
        jsonAttributeIfNotNull("maskPosition", maskPosition)
    }
}
