package ski.gagar.vertigram.types

import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

object PassportSerializationTest : BaseSerializationTest() {
    @Test
    fun `passport encrypted element should survive serialization`() {
        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.PersonalDetails(
                data = "data",
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.Passport(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.DriverLicense(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                reverseSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.IdentityCard(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                reverseSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.InternalPassport(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.Address(
                data = "data",
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.UtilityBill(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.BankStatement(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.RentalAgreement(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.PassportRegistration(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.TemporaryRegistration(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.PhoneNumber(
                phoneNumber = "+491111111111",
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.Email(
                email = "bob@gmail.com",
                hash = "hash"
            )
        )
    }

    @Test
    fun `element error should survive serialization`() {
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.DataField(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fieldName = "a",
                dataHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.FrontSide(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.ReverseSide(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.Selfie(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.File(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.Files(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHashes = listOf(),
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.TranslationFile(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.TranslationFiles(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHashes = listOf(),
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.Unspecified(
                type = Passport.EncryptedElement.Type.PASSPORT,
                elementHash = "1",
                message = "a"
            )
        )
    }

    private val DUMMY_PASSPORT_FILE = Passport.File(
        fileId = "1",
        fileUniqueId = "1",
        fileDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
        fileSize = 100
    )
}