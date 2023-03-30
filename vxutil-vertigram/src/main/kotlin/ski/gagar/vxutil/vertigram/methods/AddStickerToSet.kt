package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.InputSticker
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia

@TgMethod
data class AddStickerToSet(
    val userId: Long,
    val name: String,
    @TgMedia
    val sticker: InputSticker
) : MultipartTgCallable<Boolean>()
