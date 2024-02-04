package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.InputSticker
import ski.gagar.vertigram.types.StickerFormat
import ski.gagar.vertigram.types.StickerType
import ski.gagar.vertigram.util.multipart.TgMedia

@TgMethod
data class CreateNewStickerSet(
    val userId: Long,
    val name: String,
    val title: String,
    @TgMedia
    val stickers: List<InputSticker>,
    val stickerFormat: StickerFormat,
    val stickerType: StickerType = Defaults.stickerType,
    val needsRepainting: Boolean = false
) : MultipartTgCallable<Boolean>() {
    object Defaults {
        val stickerType = StickerType.REGULAR
    }
}
