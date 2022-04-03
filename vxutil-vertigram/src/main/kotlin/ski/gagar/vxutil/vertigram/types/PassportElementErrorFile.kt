package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type PassportElementErrorFile.
 */
data class PassportElementErrorFile(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError() {
    override val source: PassportElementErrorSource = PassportElementErrorSource.FILE
}
