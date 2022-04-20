package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.MaskPosition
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia

@TgMethod
data class CreateNewStickerSet(
    val userId: Long,
    val name: String,
    val title: String,
    val emojis: String,
    @TgMedia
    val pngSticker: Attachment? = null,
    @TgMedia
    val tgsSticker: Attachment? = null,
    @TgMedia
    val webmSticker: Attachment? = null,
    val containsMasks: Boolean? = null,
    val maskPosition: MaskPosition? = null
) : MultipartTgCallable<Boolean>()
