package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type PassportElementErrorFiles.
 */
data class PassportElementErrorFiles(
    val type: EncryptedPassportElementType,
    val fileHashes: List<String>,
    val message: String
) : PassportElementError() {
    override val source: PassportElementErrorSource = PassportElementErrorSource.FILES
}
