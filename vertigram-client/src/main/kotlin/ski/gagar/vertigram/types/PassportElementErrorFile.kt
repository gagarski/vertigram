package ski.gagar.vertigram.types

data class PassportElementErrorFile(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.FILE
}