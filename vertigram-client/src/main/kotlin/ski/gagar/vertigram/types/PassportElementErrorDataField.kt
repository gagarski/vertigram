package ski.gagar.vertigram.types

data class PassportElementErrorDataField(
    val type: EncryptedPassportElement.Type,
    val fieldName: String,
    val dataHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.DATA_FIELD
}
