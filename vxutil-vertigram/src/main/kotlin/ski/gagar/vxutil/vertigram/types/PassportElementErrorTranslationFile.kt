package ski.gagar.vxutil.vertigram.types

data class PassportElementErrorTranslationFile(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.TRANSLATION_FILE
}
