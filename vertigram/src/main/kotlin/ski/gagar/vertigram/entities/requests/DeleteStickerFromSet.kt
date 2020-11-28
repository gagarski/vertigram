package ski.gagar.vertigram.entities.requests

data class DeleteStickerFromSet(
    val sticker: String
) : JsonTgCallable<Boolean>()

