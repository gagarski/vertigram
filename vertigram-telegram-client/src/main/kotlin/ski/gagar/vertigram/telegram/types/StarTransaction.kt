package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Duration
import java.time.Instant

/**
 * Telegram [StarTransaction](https://core.telegram.org/bots/api#startransaction) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class StarTransaction internal constructor(
    val id: String,
    val amount: Int,
    val nanostarAmount: Int,
    val date: Instant,
    val source: TransactionPartner? = null,
    val target: TransactionPartner? = null
) {
    @get:JsonIgnore
    val starAmount
        get() = StarAmount(amount, nanostarAmount)

    @get:JsonIgnore
    val bigDecimalAmount
        get() = starAmount.bigDecimalValue

    /**
     * Telegram [TransactionPartner](https://core.telegram.org/bots/api#transactionpartner) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = TransactionPartner.User::class, name = TransactionPartner.Type.USER_STR),
        JsonSubTypes.Type(value = TransactionPartner.Chat::class, name = TransactionPartner.Type.CHAT_STR),
        JsonSubTypes.Type(value = TransactionPartner.AffiliateProgram::class, name = TransactionPartner.Type.AFFILIATE_PROGRAM_STR),
        JsonSubTypes.Type(value = TransactionPartner.Fragment::class, name = TransactionPartner.Type.FRAGMENT_STR),
        JsonSubTypes.Type(value = TransactionPartner.TelegramAds::class, name = TransactionPartner.Type.TELEGRAM_ADS_STR),
        JsonSubTypes.Type(value = TransactionPartner.TelegramApi::class, name = TransactionPartner.Type.TELEGRAM_API_STR),
        JsonSubTypes.Type(value = TransactionPartner.Other::class, name = TransactionPartner.Type.OTHER_STR),
    )
    sealed interface TransactionPartner {
        val type: Type
        /**
         * Telegram [TransactionPartnerUser](https://core.telegram.org/bots/api#transactionpartneruser) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class User internal constructor(
            val transactionType: TransactionType, // TODO: type hierarchies are not supported with JsonSubTypes, think about it
            val user: ski.gagar.vertigram.telegram.types.User,
            val affiliate: AffiliateInfo? = null,
            val invoicePayload: String? = null,
            val subscriptionPeriod: Duration? = null,
            val paidMedia: List<PaidMedia>? = null,
            val paidMediaPayload: String? = null,
            val gift: Gift? = null,
            val premiumSubscriptionDuration: Int? = null
        ) : TransactionPartner {
            override val type: Type = Type.USER

            /**
             * Telegram [AffiliateInfo](https://core.telegram.org/bots/api#affiliateinfo) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class AffiliateInfo internal constructor(
                val affiliateUser: ski.gagar.vertigram.telegram.types.User? = null,
                val affiliateChat: ski.gagar.vertigram.telegram.types.Chat? = null,
                val commissionPerMile: Int,
                val amount: Int,
                val nanostarAmount: Int
            ) {
                companion object
            }

            enum class TransactionType {
                @JsonProperty(INVOICE_PAYMENT_STR)
                INVOICE_PAYMENT,
                @JsonProperty(PAID_MEDIA_PAYMENT_STR)
                PAID_MEDIA_PAYMENT,
                @JsonProperty(GIFT_PURCHASE_STR)
                GIFT_PURCHASE,
                @JsonProperty(PREMIUM_PURCHASE_STR)
                PREMIUM_PURCHASE;

                companion object {
                    const val INVOICE_PAYMENT_STR = "invoice_payment"
                    const val PAID_MEDIA_PAYMENT_STR = "paid_media_payment"
                    const val GIFT_PURCHASE_STR = "gift_purchase_str"
                    const val PREMIUM_PURCHASE_STR = "premium_purchase_str"
                }
            }

            companion object
        }

        /**
         * Telegram [TransactionPartnerChat](https://core.telegram.org/bots/api#transactionpartnerchat) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Chat internal constructor(
            val chat: ski.gagar.vertigram.telegram.types.Chat,
            val gift: Gift? = null,
        ) : TransactionPartner {
            override val type: Type = Type.CHAT

            companion object
        }

        /**
         * Telegram [TransactionPartnerAffiliateProgram](https://core.telegram.org/bots/api#transactionpartneraffiliateprogram) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class AffiliateProgram internal constructor(
            val sponsorUser: ski.gagar.vertigram.telegram.types.User? = null,
            val commissionPerMile: Int,
        ) : TransactionPartner {
            override val type: Type = Type.AFFILIATE_PROGRAM

            companion object
        }

        /**
         * Telegram [TransactionPartnerFragment](https://core.telegram.org/bots/api#transactionpartnerfragment) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Fragment internal constructor(
            val sponsorUser: ski.gagar.vertigram.telegram.types.User? = null,
            val commissionPerMile: Int,
            val withdrawalState: RevenueWithdrawalState? = null
        ) : TransactionPartner {
            override val type: Type = Type.FRAGMENT

            /**
             * Telegram [RevenueWithdrawalState](https://core.telegram.org/bots/api#revenuewithdrawalstate) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
            @JsonSubTypes(
                JsonSubTypes.Type(value = RevenueWithdrawalState.Pending::class, name = RevenueWithdrawalState.Type.PENDING_STR),
                JsonSubTypes.Type(value = RevenueWithdrawalState.Succeeded::class, name = RevenueWithdrawalState.Type.SUCCEEDED_STR),
                JsonSubTypes.Type(value = RevenueWithdrawalState.Failed::class, name = RevenueWithdrawalState.Type.FAILED_STR)
            )
            sealed interface RevenueWithdrawalState {
                val type: Type

                /**
                 * Telegram [RevenueWithdrawalStatePending](https://core.telegram.org/bots/api#revenuewithdrawalstatepending) type.
                 *
                 * For up-to-date documentation, please consult the official Telegram docs.
                 */
                data object Pending : RevenueWithdrawalState {
                    override val type: Type = Type.PENDING
                }

                /**
                 * Telegram [RevenueWithdrawalStateSucceeded](https://core.telegram.org/bots/api#revenuewithdrawalstatesucceded) type.
                 *
                 * For up-to-date documentation, please consult the official Telegram docs.
                 */
                @TelegramCodegen.Type
                data class Succeeded internal constructor(
                    val date: Instant,
                    val url: String
                ) : RevenueWithdrawalState {
                    override val type: Type = Type.SUCCEEDED

                    companion object
                }

                /**
                 * Telegram [RevenueWithdrawalStateFailed](https://core.telegram.org/bots/api#revenuewithdrawalstatefailed) type.
                 *
                 * For up-to-date documentation, please consult the official Telegram docs.
                 */
                data object Failed : RevenueWithdrawalState {
                    override val type: Type = Type.FAILED
                }

                /**
                 * Value for [type]
                 */
                enum class Type {
                    @JsonProperty(PENDING_STR)
                    PENDING,
                    @JsonProperty(SUCCEEDED_STR)
                    SUCCEEDED,
                    @JsonProperty(Companion.FAILED_STR)
                    FAILED;

                    companion object {
                        const val PENDING_STR = "pending"
                        const val SUCCEEDED_STR = "succeeded"
                        const val FAILED_STR = "failed"
                    }
                }
            }

            companion object
        }

        /**
         * Telegram [TransactionPartnerTelegramAds](https://core.telegram.org/bots/api#transactionpartnertelegramads) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        data object TelegramAds : TransactionPartner {
            override val type: Type = Type.TELEGRAM_ADS
        }

        /**
         * Telegram [TransactionPartnerTelegramApi](https://core.telegram.org/bots/api#transactionpartnertelegramapi) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class TelegramApi internal constructor(
            val requestCount: Int
        ) : TransactionPartner {
            override val type: Type = Type.TELEGRAM_API

            companion object
        }

        /**
         * Telegram [TransactionPartnerOther](https://core.telegram.org/bots/api#transactionpartnerother) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        data object Other : TransactionPartner {
            override val type: Type = Type.OTHER
        }

        /**
         * Value for [type]
         */
        enum class Type {
            @JsonProperty(USER_STR)
            USER,
            @JsonProperty(CHAT_STR)
            CHAT,
            @JsonProperty(AFFILIATE_PROGRAM_STR)
            AFFILIATE_PROGRAM,
            @JsonProperty(FRAGMENT_STR)
            FRAGMENT,
            @JsonProperty(TELEGRAM_ADS_STR)
            TELEGRAM_ADS,
            @JsonProperty(TELEGRAM_API_STR)
            TELEGRAM_API,
            @JsonProperty(OTHER_STR)
            OTHER,
            ;
            companion object {
                const val USER_STR = "user"
                const val CHAT_STR = "chat"
                const val AFFILIATE_PROGRAM_STR = "affiliate_program"
                const val FRAGMENT_STR = "fragment"
                const val TELEGRAM_ADS_STR = "ads"
                const val TELEGRAM_API_STR = "telegram_api"
                const val OTHER_STR = "other"
            }
        }
    }

    companion object
}