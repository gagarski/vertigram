package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type PassportElementErrorTranslationFiles.
 */
data class PassportElementErrorTranslationFiles(
    val type: EncryptedPassportElementType,
    val fileHashes: List<String>,
    val message: String
) : PassportElementError() {
    override val source: PassportElementErrorSource = PassportElementErrorSource.TRANSLATION_FILES
}
