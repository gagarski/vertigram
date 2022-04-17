package ski.gagar.vxutil.vertigram.types

data class PassportElementErrorUnspecified(
    val type: EncryptedPassportElementType,
    val elementHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.UNSPECIFIED
}
