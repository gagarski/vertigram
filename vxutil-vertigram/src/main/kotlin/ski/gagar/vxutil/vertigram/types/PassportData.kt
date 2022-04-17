package ski.gagar.vxutil.vertigram.types

data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
)
