package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type PassportData.
 */
data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
)
