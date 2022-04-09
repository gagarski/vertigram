package ski.gagar.vxutil.vertigram.methods

data class SetStickerPositionInSet(
    val sticker: String,
    val position: Integer
) : JsonTgCallable<Boolean>()

