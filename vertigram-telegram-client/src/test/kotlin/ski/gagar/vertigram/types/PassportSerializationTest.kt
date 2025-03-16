package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import java.time.Instant
import java.time.temporal.ChronoUnit

object PassportSerializationTest : BaseSerializationTest() {
    @Test
    fun `passport encrypted element should survive serialization`() {
        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.PersonalDetails.create(
                data = "data",
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.Passport.create(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.DriverLicense.create(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                reverseSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.IdentityCard.create(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                reverseSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.InternalPassport.create(
                data = "data",
                frontSide = DUMMY_PASSPORT_FILE,
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.Address.create(
                data = "data",
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.UtilityBill.create(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.BankStatement.create(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.RentalAgreement.create(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.PassportRegistration.create(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.TemporaryRegistration.create(
                files = listOf(DUMMY_PASSPORT_FILE),
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.PhoneNumber.create(
                phoneNumber = "+491111111111",
                hash = "hash"
            )
        )

        assertSerializable<Passport.EncryptedElement>(
            Passport.EncryptedElement.Email.create(
                email = "bob@gmail.com",
                hash = "hash"
            )
        )
    }

    @Test
    fun `element error should survive serialization`() {
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.DataField.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fieldName = "a",
                dataHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.FrontSide.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.ReverseSide.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.Selfie.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.File.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.Files.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHashes = listOf(),
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.TranslationFile.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHash = "1",
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.TranslationFiles.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                fileHashes = listOf(),
                message = "a"
            )
        )
        assertSerializable<Passport.ElementError>(
            Passport.ElementError.Unspecified.create(
                type = Passport.EncryptedElement.Type.PASSPORT,
                elementHash = "1",
                message = "a"
            )
        )
    }

    private val DUMMY_PASSPORT_FILE = Passport.File.create(
        fileId = "1",
        fileUniqueId = "1",
        fileDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
        fileSize = 100
    )
}