package ski.gagar.vxutil.vertigram.entities.requests

data class DeleteStickerFromSet(
    val sticker: String
) : JsonTgCallable<Boolean>()

