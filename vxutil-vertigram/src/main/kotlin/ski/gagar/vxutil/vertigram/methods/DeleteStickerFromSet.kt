package ski.gagar.vxutil.vertigram.methods

data class DeleteStickerFromSet(
    val sticker: String
) : JsonTgCallable<Boolean>()

