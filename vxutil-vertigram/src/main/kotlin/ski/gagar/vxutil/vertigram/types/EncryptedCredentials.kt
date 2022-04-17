package ski.gagar.vxutil.vertigram.types

data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)
