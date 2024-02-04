package ski.gagar.vertigram.types

data class PassportElementErrorTranslationFiles(
    val type: EncryptedPassportElementType,
    val fileHashes: List<String>,
    val message: String
) : PassportElementError {
    override val source: PassportElementErrorSource = PassportElementErrorSource.TRANSLATION_FILES
}
