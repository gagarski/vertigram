package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class EncryptedPassportElementType {
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
