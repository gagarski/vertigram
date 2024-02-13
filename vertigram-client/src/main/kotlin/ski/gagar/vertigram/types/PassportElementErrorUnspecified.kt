package ski.gagar.vertigram.types

data class PassportElementErrorUnspecified(
    val type: EncryptedPassportElement.Type,
    val elementHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.UNSPECIFIED
}
