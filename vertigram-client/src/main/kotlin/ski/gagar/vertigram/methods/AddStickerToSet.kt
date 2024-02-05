package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.InputSticker
import ski.gagar.vertigram.util.multipart.TgMedia

@TgMethod
data class AddStickerToSet(
    val userId: Long,
    val name: String,
    @TgMedia
    val sticker: InputSticker
) : MultipartTgCallable<Boolean>()
