package ski.gagar.vertigram.types

data class PassportElementErrorSelfie(
    val type: EncryptedPassportElement.Type,
    val fileHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.SELFIE
}
