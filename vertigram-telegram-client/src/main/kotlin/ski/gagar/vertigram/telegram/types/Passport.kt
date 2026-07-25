package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Contains classes related to Telegram Passport
 */
object Passport {
    /**
     * Describes a Telegram Passport element shared with the bot.
     *
     * Each subtype represents one element type and its applicable fields.
     *
     * See Telegram's
     * [EncryptedPassportElement](https://core.telegram.org/bots/api#encryptedpassportelement) documentation.
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

        /** Case when the element contains encrypted personal details. */
        @TelegramCodegen.Type
        data class PersonalDetails internal constructor(
            /** Base64-encoded encrypted element data. */
            val data: String,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PERSONAL_DETAILS

            companion object
        }

        /** Case when the element contains an encrypted passport. */
        @TelegramCodegen.Type
        data class Passport internal constructor(
            /** Base64-encoded encrypted element data. */
            val data: String,
            /** Encrypted file with the front side of the document. */
            val frontSide: File,
            /** Encrypted file with the selfie of the user holding the document. */
            val selfie: File? = null,
            /** Encrypted files with translated versions of the document. */
            val translation: List<File>? = null,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PASSPORT

            companion object
        }

        /** Case when the element contains an encrypted driver license. */
        @TelegramCodegen.Type
        data class DriverLicense internal constructor(
            /** Base64-encoded encrypted element data. */
            val data: String,
            /** Encrypted file with the front side of the document. */
            val frontSide: File,
            /** Encrypted file with the reverse side of the document. */
            val reverseSide: File,
            /** Encrypted file with the selfie of the user holding the document. */
            val selfie: File? = null,
            /** Encrypted files with translated versions of the document. */
            val translation: List<File>? = null,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.DRIVER_LICENSE

            companion object
        }

        /** Case when the element contains an encrypted identity card. */
        @TelegramCodegen.Type
        data class IdentityCard internal constructor(
            /** Base64-encoded encrypted element data. */
            val data: String,
            /** Encrypted file with the front side of the document. */
            val frontSide: File,
            /** Encrypted file with the reverse side of the document. */
            val reverseSide: File,
            /** Encrypted file with the selfie of the user holding the document. */
            val selfie: File? = null,
            /** Encrypted files with translated versions of the document. */
            val translation: List<File>? = null,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.IDENTITY_CARD

            companion object
        }

        /** Case when the element contains an encrypted internal passport. */
        @TelegramCodegen.Type
        data class InternalPassport internal constructor(
            /** Base64-encoded encrypted element data. */
            val data: String,
            /** Encrypted file with the front side of the document. */
            val frontSide: File,
            /** Encrypted file with the selfie of the user holding the document. */
            val selfie: File? = null,
            /** Encrypted files with translated versions of the document. */
            val translation: List<File>? = null,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.INTERNAL_PASSPORT

            companion object
        }

        /** Case when the element contains an encrypted address. */
        @TelegramCodegen.Type
        data class Address internal constructor(
            /** Base64-encoded encrypted element data. */
            val data: String,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.ADDRESS

            companion object
        }

        /** Case when the element contains encrypted utility bills. */
        @TelegramCodegen.Type
        data class UtilityBill internal constructor(
            /** Encrypted files with the document. */
            val files: List<File>,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.UTILITY_BILL

            companion object
        }

        /** Case when the element contains encrypted bank statements. */
        @TelegramCodegen.Type
        data class BankStatement internal constructor(
            /** Encrypted files with the document. */
            val files: List<File>,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.BANK_STATEMENT

            companion object
        }

        /** Case when the element contains encrypted rental agreements. */
        @TelegramCodegen.Type
        data class RentalAgreement internal constructor(
            /** Encrypted files with the document. */
            val files: List<File>,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.RENTAL_AGREEMENT

            companion object
        }

        /** Case when the element contains encrypted passport registration pages. */
        @TelegramCodegen.Type
        data class PassportRegistration internal constructor(
            /** Encrypted files with the document. */
            val files: List<File>,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PASSPORT_REGISTRATION

            companion object
        }

        /** Case when the element contains encrypted temporary registration pages. */
        @TelegramCodegen.Type
        data class TemporaryRegistration internal constructor(
            /** Encrypted files with the document. */
            val files: List<File>,
            /** Base64-encoded element hash for decryption and authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.TEMPORARY_REGISTRATION

            companion object
        }

        /** Case when the element contains an unencrypted phone number. */
        @TelegramCodegen.Type
        data class PhoneNumber internal constructor(
            /** User's verified phone number. */
            val phoneNumber: String,
            /** Base64-encoded element hash for authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.PHONE_NUMBER

            companion object
        }

        /** Case when the element contains an unencrypted email address. */
        @TelegramCodegen.Type
        data class Email internal constructor(
            /** User's verified email address. */
            val email: String,
            /** Base64-encoded element hash for authentication. */
            val hash: String
        ) : EncryptedElement {
            override val type: Type = Type.EMAIL

            companion object
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
     * Describes encrypted credentials required to decrypt Telegram Passport data.
     *
     * See Telegram's [EncryptedCredentials](https://core.telegram.org/bots/api#encryptedcredentials) documentation.
     */
    @TelegramCodegen.Type
    data class EncryptedCredentials internal constructor(
        /** Base64-encoded encrypted credentials. */
        val data: String,
        /** Base64-encoded data hash for authentication. */
        val hash: String,
        /** Base64-encoded encrypted secret required to decrypt the data. */
        val secret: String
    ) {
        companion object
    }

    /**
     * Describes Telegram Passport data shared with the bot.
     *
     * See Telegram's [PassportData](https://core.telegram.org/bots/api#passportdata) documentation.
     */
    @TelegramCodegen.Type
    data class Data internal constructor(
        /** Encrypted information about documents and other Telegram Passport elements. */
        val data: List<EncryptedElement>,
        /** Encrypted credentials required to decrypt [data]. */
        val credentials: EncryptedCredentials
    ) {
        companion object
    }

    /**
     * Represents a file uploaded to Telegram Passport.
     *
     * See Telegram's [PassportFile](https://core.telegram.org/bots/api#passportfile) documentation.
     */
    @TelegramCodegen.Type
    data class File internal constructor(
        /** Identifier for downloading this file. */
        val fileId: String,
        /** Unique identifier for this file. */
        val fileUniqueId: String,
        /** File size in bytes. */
        val fileSize: Long,
        /** Date the file was uploaded. */
        val fileDate: Instant
    ) {
        companion object
    }

    /**
     * Represents an error in a Telegram Passport element submitted by the user.
     *
     * See Telegram's [PassportElementError](https://core.telegram.org/bots/api#passportelementerror) documentation.
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
         * Case when the error concerns a data field.
         *
         * See Telegram's
         * [PassportElementErrorDataField](https://core.telegram.org/bots/api#passportelementerrordatafield)
         * documentation.
         */
        @TelegramCodegen.Type
        data class DataField internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Name of the data field that has the issue. */
            val fieldName: String,
            /** Base64-encoded hash of the data. */
            val dataHash: String,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.DATA_FIELD

            companion object
        }

        /**
         * Case when the error concerns the front side of a document.
         *
         * See Telegram's
         * [PassportElementErrorFrontSide](https://core.telegram.org/bots/api#passportelementerrorfrontside)
         * documentation.
         */
        @TelegramCodegen.Type
        data class FrontSide internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded hash of the file with the front side. */
            val fileHash: String,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.FRONT_SIDE

            companion object
        }

        /**
         * Case when the error concerns the reverse side of a document.
         *
         * See Telegram's
         * [PassportElementErrorReverseSide](https://core.telegram.org/bots/api#passportelementerrorreverseside)
         * documentation.
         */
        @TelegramCodegen.Type
        data class ReverseSide internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded hash of the file with the reverse side. */
            val fileHash: String,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.REVERSE_SIDE

            companion object
        }

        /**
         * Case when the error concerns a selfie with a document.
         *
         * See Telegram's [PassportElementErrorSelfie](https://core.telegram.org/bots/api#passportelementerrorselfie)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Selfie internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded hash of the file with the selfie. */
            val fileHash: String,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.SELFIE

            companion object
        }

        /**
         * Case when the error concerns one document file.
         *
         * See Telegram's [PassportElementErrorFile](https://core.telegram.org/bots/api#passportelementerrorfile)
         * documentation.
         */
        @TelegramCodegen.Type
        data class File internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded hash of the file. */
            val fileHash: String,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.FILE

            companion object
        }

        /**
         * Case when the error concerns a list of document files.
         *
         * See Telegram's [PassportElementErrorFiles](https://core.telegram.org/bots/api#passportelementerrorfiles)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Files internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded hashes of the files. */
            val fileHashes: List<String>,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.FILES

            companion object
        }

        /**
         * Case when the error concerns one translated document file.
         *
         * See Telegram's
         * [PassportElementErrorTranslationFile](https://core.telegram.org/bots/api#passportelementerrortranslationfile)
         * documentation.
         */
        @TelegramCodegen.Type
        data class TranslationFile internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded hash of the translation file. */
            val fileHash: String,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.TRANSLATION_FILE

            companion object
        }

        /**
         * Case when the error concerns a list of translated document files.
         *
         * See Telegram's
         * [translation files error](https://core.telegram.org/bots/api#passportelementerrortranslationfiles)
         * documentation.
         */
        @TelegramCodegen.Type
        data class TranslationFiles internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded hashes of the translation files. */
            val fileHashes: List<String>,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.TRANSLATION_FILES

            companion object
        }

        /**
         * Case when the error concerns an unspecified element.
         *
         * See Telegram's
         * [PassportElementErrorUnspecified](https://core.telegram.org/bots/api#passportelementerrorunspecified)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Unspecified internal constructor(
            /** Type of the Telegram Passport element that has the issue. */
            val type: EncryptedElement.Type,
            /** Base64-encoded element hash. */
            val elementHash: String,
            /** Error message. */
            val message: String
        ) : ElementError {
            override val source: Source = Source.UNSPECIFIED

            companion object
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
