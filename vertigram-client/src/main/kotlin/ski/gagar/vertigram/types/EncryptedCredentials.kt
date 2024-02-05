package ski.gagar.vertigram.types

data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)
