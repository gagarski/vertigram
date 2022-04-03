package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for [EncryptedPassportElement.type]
 */
enum class EncryptedPassportElementType {
    @TgEnumName(PERSONAL_DETAILS_STR)
    PERSONAL_DETAILS,
    @TgEnumName(PASSPORT_STR)
    PASSPORT,
    @TgEnumName(DRIVER_LICENSE_STR)
    DRIVER_LICENSE,
    @TgEnumName(IDENTITY_CARD_STR)
    IDENTITY_CARD,
    @TgEnumName(INTERNAL_PASSPORT_STR)
    INTERNAL_PASSPORT,
    @TgEnumName(ADDRESS_STR)
    ADDRESS,
    @TgEnumName(UTILITY_BILL_STR)
    UTILITY_BILL,
    @TgEnumName(BANK_STATEMENT_STR)
    BANK_STATEMENT,
    @TgEnumName(RENTAL_AGREEMENT_STR)
    RENTAL_AGREEMENT,
    @TgEnumName(PASSPORT_REGISTRATION_STR)
    PASSPORT_REGISTRATION,
    @TgEnumName(TEMPORARY_REGISTRATION_STR)
    TEMPORARY_REGISTRATION,
    @TgEnumName(PHONE_NUMBER_STR)
    PHONE_NUMBER,
    @TgEnumName(EMAIL_STR)
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
