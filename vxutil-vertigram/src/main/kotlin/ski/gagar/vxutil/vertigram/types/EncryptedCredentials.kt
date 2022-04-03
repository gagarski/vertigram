package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type EncryptedCredentials.
 */
data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)
