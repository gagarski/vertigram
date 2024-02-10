package ski.gagar.vertigram.methods

data class SetStickerPositionInSet(
    val sticker: String,
    val position: Int
) : JsonTelegramCallable<Boolean>()

