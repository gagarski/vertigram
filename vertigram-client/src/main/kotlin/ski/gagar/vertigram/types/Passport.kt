package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Contains classes related to Telegram Passport
 */
object Passport {
    /**
     * Telegram [EncryptedPassportElement](https://core.telegram.org/bots/api#encryptedpassportelement) type.
     *
     * Subtypes are introduced to represent document types for [type], given each of them has its own set
     * of mandatory an optional fields described [here](https://core.telegram.org/passport#fields).
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = EncryptedElement.PersonalDetails::class, name = EncryptedElement.Type.PERSONAL_DETAILS_STR),
        JsonSubTypes.Type(value = EncryptedElement.Passport::class, name = EncryptedElement.Type.PASSPORT_STR),
        JsonSubTypes.Type(value = EncryptedElement.DriverLicense::class, name = EncryptedElement.Type.DRIVER_LICENSE_STR),
        JsonSubTypes.Type(value = EncryptedElement.IdentityCard::class, name = EncryptedElement.Type.IDENTITY_CARD_STR),
        JsonSubTypes.Type(value = EncryptedElement.InternalPassport::class, name = EncryptedElement.Type.INTERNAL_PASSPORT_STR),
        JsonSubTypes.Type(value = EncryptedElement.Address::class, name = EncryptedElement.Type.ADDRESS_STR),
        JsonSubTypes.Type(value = EncryptedElement.UtilityBill::class, name = EncryptedElement.Type.UTILITY_BILL_STR),
        JsonSubTypes.Type(value = EncryptedElement.BankStatement::class, name = EncryptedElement.Type.BANK_STATEMENT_STR),
        JsonSubTypes.Type(value = EncryptedElement.RentalAgreement::class, name = EncryptedElement.Type.RENTAL_AGREEMENT_STR),
        JsonSubTypes.Type(value = EncryptedElement.PassportRegistration::class, name = EncryptedElement.Type.PASSPORT_REGISTRATION_STR),
        JsonSubTypes.Type(value = EncryptedElement.TemporaryRegistration::class, name = EncryptedElement.Type.TEMPORARY_REGISTRATION_STR),
        JsonSubTypes.Type(value = EncryptedElement.PhoneNumber::class, name = EncryptedElement.Type.PHONE_NUMBER_STR),
        JsonSubTypes.Type(value = EncryptedElement.Email::class, name = EncryptedElement.Type.EMAIL_STR),
    )
    interface EncryptedElement {
        val type: Type

        /**
         * Personal Details case
         */
        data class PersonalDetails(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val data: String,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PERSONAL_DETAILS
        }

        /**
         * Passport case
         */
        data class Passport(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val data: String,
            val frontSide: File,
            val selfie: File? = null,
            val translation: List<File>? = null,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PASSPORT
        }

        /**
         * Driver License case
         */
        data class DriverLicense(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val data: String,
            val frontSide: File,
            val reverseSide: File,
            val selfie: File? = null,
            val translation: List<File>? = null,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.DRIVER_LICENSE
        }

        /**
         * Identity Card case
         */
        data class IdentityCard(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val data: String,
            val frontSide: File,
            val reverseSide: File,
            val selfie: File? = null,
            val translation: List<File>? = null,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.IDENTITY_CARD
        }

        /**
         * Internal Passport case
         */
        data class InternalPassport(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val data: String,
            val frontSide: File,
            val selfie: File? = null,
            val translation: List<File>? = null,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.INTERNAL_PASSPORT
        }

        /**
         * Address case
         */
        data class Address(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val data: String,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.ADDRESS
        }

        /**
         * Utility Bill case
         */
        data class UtilityBill(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val files: List<File>,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.UTILITY_BILL
        }

        /**
         * Bank Statement case
         */
        data class BankStatement(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val files: List<File>,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.BANK_STATEMENT
        }

        /**
         * Rental Agreement case
         */
        data class RentalAgreement(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val files: List<File>,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.RENTAL_AGREEMENT
        }

        /**
         * Passport Registration case
         */
        data class PassportRegistration(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val files: List<File>,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PASSPORT_REGISTRATION
        }

        /**
         * Temporary Registration case
         */
        data class TemporaryRegistration(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val files: List<File>,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.TEMPORARY_REGISTRATION
        }

        /**
         * Phone Number case
         */
        data class PhoneNumber(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val phoneNumber: String,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PHONE_NUMBER
        }

        /**
         * Phone Number case
         */
        data class Email(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val email: String,
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.EMAIL
        }

        /**
         * A value for [EncryptedElement.type]
         */
        enum class Type {
            @JsonProperty(PERSONAL_DETAILS_STR)
            PERSONAL_DETAILS,
            @JsonProperty(PASSPORT_STR)
            PASSPORT,
            @JsonProperty(DRIVER_LICENSE_STR)
            DRIVER_LICENSE,
            @JsonProperty(IDENTITY_CARD_STR)
            IDENTITY_CARD,
            @JsonProperty(INTERNAL_PASSPORT_STR)
            INTERNAL_PASSPORT,
            @JsonProperty(ADDRESS_STR)
            ADDRESS,
            @JsonProperty(UTILITY_BILL_STR)
            UTILITY_BILL,
            @JsonProperty(BANK_STATEMENT_STR)
            BANK_STATEMENT,
            @JsonProperty(RENTAL_AGREEMENT_STR)
            RENTAL_AGREEMENT,
            @JsonProperty(PASSPORT_REGISTRATION_STR)
            PASSPORT_REGISTRATION,
            @JsonProperty(TEMPORARY_REGISTRATION_STR)
            TEMPORARY_REGISTRATION,
            @JsonProperty(PHONE_NUMBER_STR)
            PHONE_NUMBER,
            @JsonProperty(EMAIL_STR)
            EMAIL;

            companion object {
                const val PERSONAL_DETAILS_STR = "personal_details"
                const val PASSPORT_STR = "passport"
                const val DRIVER_LICENSE_STR = "driver_license"
                const val IDENTITY_CARD_STR = "identity_card"
                const val INTERNAL_PASSPORT_STR = "internal_passport"
                const val ADDRESS_STR = "address"
                const val UTILITY_BILL_STR = "utility_bill"
                const val BANK_STATEMENT_STR = "bank_statement"
                const val RENTAL_AGREEMENT_STR = "rental_agreement"
                const val PASSPORT_REGISTRATION_STR = "passport_registration"
                const val TEMPORARY_REGISTRATION_STR = "temporary_registration"
                const val PHONE_NUMBER_STR = "phone_number"
                const val EMAIL_STR = "email"
            }
        }
    }

    /**
     * Telegram [EncryptedCredentials](https://core.telegram.org/bots/api#encryptedcredentials) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class EncryptedCredentials(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val data: String,
        val hash: String,
        val secret: String
    )

    /**
     * Telegram [PassportData](https://core.telegram.org/bots/api#passportdata) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Data(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val data: List<EncryptedElement>,
        val credentials: EncryptedCredentials
    )

    /**
     * Telegram [PassportFile](https://core.telegram.org/bots/api#passportfile) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class File(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val fileId: String,
        val fileUniqueId: String,
        val fileSize: Long,
        val fileDate: Instant
    )


    /**
     * Telegram [PassportElementError](https://core.telegram.org/bots/api#passportelementerror) type.
     *
     * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
     * names given they are nested into [Passport.ElementError] class. The rule here is the following:
     * `PassportElementErrorXxx` Telegram type becomes `PassportElementErrorXxx`.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "source", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = ElementError.DataField::class, name = ElementError.Source.DATA_FIELD_STR),
        JsonSubTypes.Type(value = ElementError.FrontSide::class, name = ElementError.Source.FRONT_SIDE_STR),
        JsonSubTypes.Type(value = ElementError.ReverseSide::class, name = ElementError.Source.REVERSE_SIDE_STR),
        JsonSubTypes.Type(value = ElementError.Selfie::class, name = ElementError.Source.SELFIE_STR),
        JsonSubTypes.Type(value = ElementError.File::class, name = ElementError.Source.FILE_STR),
        JsonSubTypes.Type(value = ElementError.Files::class, name = ElementError.Source.FILES_STR),
        JsonSubTypes.Type(value = ElementError.TranslationFile::class, name = ElementError.Source.TRANSLATION_FILE_STR),
        JsonSubTypes.Type(value = ElementError.TranslationFiles::class, name = ElementError.Source.TRANSLATION_FILES_STR),
        JsonSubTypes.Type(value = ElementError.Unspecified::class, name = ElementError.Source.UNSPECIFIED_STR),
    )
    sealed interface ElementError {
        val source: Source

        /**
         * Telegram [PassportElementErrorDataField](https://core.telegram.org/bots/api#passportelementerrordatafield) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class DataField(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fieldName: String,
            val dataHash: String,
            val message: String
        ) : ElementError {
            override val source: Source = Source.DATA_FIELD
        }

        /**
         * Telegram [PassportElementErrorFrontSide](https://core.telegram.org/bots/api#passportelementerrorfrontside) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class FrontSide(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fileHash: String,
            val message: String
        ) : ElementError {
            override val source: Source = Source.FRONT_SIDE
        }

        /**
         * Telegram [PassportElementErrorReverseSide](https://core.telegram.org/bots/api#passportelementerrorreverseside) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class ReverseSide(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fileHash: String,
            val message: String
        ) : ElementError {
            override val source: Source = Source.REVERSE_SIDE
        }

        /**
         * Telegram [PassportElementErrorSelfie](https://core.telegram.org/bots/api#passportelementerrorselfie) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Selfie(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fileHash: String,
            val message: String
        ) : ElementError {
            override val source: Source = Source.SELFIE
        }

        /**
         * Telegram [PassportElementErrorFile](https://core.telegram.org/bots/api#passportelementerrorfile) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class File(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fileHash: String,
            val message: String
        ) : ElementError {
            override val source: Source = Source.FILE
        }

        /**
         * Telegram [PassportElementErrorFiles](https://core.telegram.org/bots/api#passportelementerrorfiles) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Files(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fileHashes: List<String>,
            val message: String
        ) : ElementError {
            override val source: Source = Source.FILES
        }

        /**
         * Telegram [PassportElementErrorTranslationFile](https://core.telegram.org/bots/api#passportelementerrortranslationfile) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class TranslationFile(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fileHash: String,
            val message: String
        ) : ElementError {
            override val source: Source = Source.TRANSLATION_FILE
        }

        /**
         * Telegram [PassportElementErrorTranslationFiles](https://core.telegram.org/bots/api#passportelementerrortranslationfiles) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class TranslationFiles(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val fileHashes: List<String>,
            val message: String
        ) : ElementError {
            override val source: Source = Source.TRANSLATION_FILES
        }

        /**
         * Telegram [PassportElementErrorUnspecified](https://core.telegram.org/bots/api#passportelementerrorunspecified) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Unspecified(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val type: EncryptedElement.Type,
            val elementHash: String,
            val message: String
        ) : ElementError {
            override val source: Source = Source.UNSPECIFIED
        }


        /**
         * Value for [source]
         */
        enum class Source {
            @JsonProperty(DATA_FIELD_STR)
            DATA_FIELD,
            @JsonProperty(FRONT_SIDE_STR)
            FRONT_SIDE,
            @JsonProperty(REVERSE_SIDE_STR)
            REVERSE_SIDE,
            @JsonProperty(SELFIE_STR)
            SELFIE,
            @JsonProperty(FILE_STR)
            FILE,
            @JsonProperty(FILES_STR)
            FILES,
            @JsonProperty(TRANSLATION_FILE_STR)
            TRANSLATION_FILE,
            @JsonProperty(TRANSLATION_FILES_STR)
            TRANSLATION_FILES,
            @JsonProperty(UNSPECIFIED_STR)
            UNSPECIFIED;
            companion object {
                const val DATA_FIELD_STR = "data"
                const val FRONT_SIDE_STR = "front_side"
                const val REVERSE_SIDE_STR = "reverse_side"
                const val SELFIE_STR = "selfie"
                const val FILE_STR = "file"
                const val FILES_STR = "files"
                const val TRANSLATION_FILE_STR = "translation_file"
                const val TRANSLATION_FILES_STR = "translation_files"
                const val UNSPECIFIED_STR = "unspecified"
            }
        }

    }


}