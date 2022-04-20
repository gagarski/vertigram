package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.MaskPosition
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia

@TgMethod
data class AddStickerToSet(
    val userId: Long,
    val name: String,
    val emojis: String,
    @TgMedia
    val pngSticker: Attachment? = null,
    @TgMedia
    val tgsSticker: Attachment? = null,
    @TgMedia
    val webmSticker: Attachment? = null,
    val maskPosition: MaskPosition? = null
) : MultipartTgCallable<Boolean>()
