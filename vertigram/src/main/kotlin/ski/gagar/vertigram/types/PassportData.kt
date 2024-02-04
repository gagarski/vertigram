package ski.gagar.vertigram.types

data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
)
