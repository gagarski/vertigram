package ski.gagar.vxutil.vertigram.types

data class PassportElementErrorDataField(
    val type: EncryptedPassportElementType,
    val fieldName: String,
    val dataHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.DATA_FIELD
}
