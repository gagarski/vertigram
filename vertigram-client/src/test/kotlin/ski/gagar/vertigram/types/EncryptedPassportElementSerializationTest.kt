package ski.gagar.vertigram.types

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

object EncryptedPassportElementSerializationTest : BaseSerializationTest() {
    @Test
    fun `encrypted passport element should survive serialization`() {
        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.PersonalDetails(
                data = "data",
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.Passport(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.DriverLicense(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                reverseSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.IdentityCard(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                reverseSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.InternalPassport(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.Address(
                data = "data",
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.UtilityBill(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.BankStatement(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.RentalAgreement(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.PassportRegistration(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.TemporaryRegistration(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.PhoneNumber(
                phoneNumber = "+491111111111",
                hash = "hash"
            )
        )

        assertSerializable<EncryptedPassportElement>(
            EncryptedPassportElement.Email(
                email = "bob@gmail.com",
                hash = "hash"
            )
        )
    }


    private val DUMMY_PASSPORT_FILE = PassportFile(
        fileId = "1",
        fileUniqueId = "1",
        fileDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
        fileSize = 100
    )
}