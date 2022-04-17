package ski.gagar.vxutil.vertigram.types

data class PassportElementErrorFrontSide(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.FRONT_SIDE
}
