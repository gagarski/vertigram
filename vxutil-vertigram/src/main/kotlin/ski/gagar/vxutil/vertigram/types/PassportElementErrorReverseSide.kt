package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type PassportElementErrorReverseSide.
 */
data class PassportElementErrorReverseSide(
    val type: EncryptedPassportElementType,
    val fileHash: String,
    val message: String
) : PassportElementError() {
    override val source: PassportElementErrorSource = PassportElementErrorSource.REVERSE_SIDE
}
