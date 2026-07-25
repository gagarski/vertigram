package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Duration
import java.time.Instant

/**
 * Describes a Telegram Star transaction.
 *
 * See Telegram's [StarTransaction](https://core.telegram.org/bots/api#startransaction) documentation.
 */
@TelegramCodegen.Type
data class StarTransaction internal constructor(
    /** Unique identifier of the transaction. */
    val id: String,
    /** Integer amount of Telegram Stars transferred by the transaction. */
    val amount: Int,
    /** Number of 1-billionth shares of a Telegram Star transferred by the transaction. */
    val nanostarAmount: Int,
    /** Date the transaction was created. */
    val date: Instant,
    /** Source of an incoming transaction. */
    val source: TransactionPartner? = null,
    /** Receiver of an outgoing transaction. */
    val receiver: TransactionPartner? = null
) {
    @get:JsonIgnore
    val starAmount
        get() = StarAmount(amount, nanostarAmount)

    @get:JsonIgnore
    val bigDecimalAmount
        get() = starAmount.bigDecimalValue

    /**
     * Describes the source of an incoming transaction or the receiver of an outgoing transaction.
     *
     * See Telegram's [TransactionPartner](https://core.telegram.org/bots/api#transactionpartner) documentation.
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
         * Case when the transaction partner is a user.
         *
         * See Telegram's [TransactionPartnerUser](https://core.telegram.org/bots/api#transactionpartneruser)
         * documentation.
         */
        @TelegramCodegen.Type
        data class User internal constructor(
            /** Type of the transaction. */
            val transactionType: TransactionType, // TODO: type hierarchies are not supported with JsonSubTypes, think about it
            /** User involved in the transaction. */
            val user: ski.gagar.vertigram.telegram.types.User,
            /** Information about the affiliate that received a commission. */
            val affiliate: AffiliateInfo? = null,
            /** Bot-specified invoice payload. */
            val invoicePayload: String? = null,
            /** Duration of the paid subscription. */
            val subscriptionPeriod: Duration? = null,
            /** Paid media bought by the user. */
            val paidMedia: List<PaidMedia>? = null,
            /** Bot-specified paid-media payload. */
            val paidMediaPayload: String? = null,
            /** Gift involved in the transaction. */
            val gift: Gift? = null,
            /** Duration of the Telegram Premium subscription in months. */
            val premiumSubscriptionDuration: Int? = null
        ) : TransactionPartner {
            override val type: Type = Type.USER

            /**
             * Contains information about the affiliate that received a commission.
             *
             * See Telegram's [AffiliateInfo](https://core.telegram.org/bots/api#affiliateinfo) documentation.
             */
            @TelegramCodegen.Type
            data class AffiliateInfo internal constructor(
                /** User that received the commission. */
                val affiliateUser: ski.gagar.vertigram.telegram.types.User? = null,
                /** Chat that received the commission. */
                val affiliateChat: ski.gagar.vertigram.telegram.types.Chat? = null,
                /** Number of Telegram Stars received per 1000 Stars from referred transactions. */
                val commissionPerMille: Int,
                /** Integer amount of Telegram Stars received by the affiliate. */
                val amount: Int,
                /** Number of 1-billionth shares of a Telegram Star received by the affiliate. */
                val nanostarAmount: Int
            ) {
                companion object
            }

            enum class TransactionType {
                /** Payment of an invoice. */
                @JsonProperty(INVOICE_PAYMENT_STR)
                INVOICE_PAYMENT,
                /** Payment for paid media. */
                @JsonProperty(PAID_MEDIA_PAYMENT_STR)
                PAID_MEDIA_PAYMENT,
                /** Purchase of a gift. */
                @JsonProperty(GIFT_PURCHASE_STR)
                GIFT_PURCHASE,
                /** Purchase of a Telegram Premium subscription. */
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
         * Case when the transaction partner is a chat.
         *
         * See Telegram's [TransactionPartnerChat](https://core.telegram.org/bots/api#transactionpartnerchat)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Chat internal constructor(
            /** Chat involved in the transaction. */
            val chat: ski.gagar.vertigram.telegram.types.Chat,
            /** Gift involved in the transaction. */
            val gift: Gift? = null,
        ) : TransactionPartner {
            override val type: Type = Type.CHAT

            companion object
        }

        /**
         * Case when the transaction partner is an affiliate program.
         *
         * See Telegram's
         * [TransactionPartnerAffiliateProgram](https://core.telegram.org/bots/api#transactionpartneraffiliateprogram)
         * documentation.
         */
        @TelegramCodegen.Type
        data class AffiliateProgram internal constructor(
            /** Bot or user sponsoring the affiliate program. */
            val sponsorUser: ski.gagar.vertigram.telegram.types.User? = null,
            /** Number of Telegram Stars received per 1000 Stars from referred transactions. */
            val commissionPerMille: Int,
        ) : TransactionPartner {
            override val type: Type = Type.AFFILIATE_PROGRAM

            companion object
        }

        /**
         * Case when the transaction partner is Fragment.
         *
         * See Telegram's [TransactionPartnerFragment](https://core.telegram.org/bots/api#transactionpartnerfragment)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Fragment internal constructor(
            /** User sponsoring the transaction. */
            val sponsorUser: ski.gagar.vertigram.telegram.types.User? = null,
            /** Number of Telegram Stars received per 1000 Stars from referred transactions. */
            val commissionPerMille: Int,
            /** State of the withdrawal. */
            val withdrawalState: RevenueWithdrawalState? = null
        ) : TransactionPartner {
            override val type: Type = Type.FRAGMENT

            /**
             * Describes the state of a revenue withdrawal operation.
             *
             * See Telegram's
             * [RevenueWithdrawalState](https://core.telegram.org/bots/api#revenuewithdrawalstate) documentation.
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
                 * Case when the withdrawal is in progress.
                 *
                 * See Telegram's
                 * [RevenueWithdrawalStatePending](https://core.telegram.org/bots/api#revenuewithdrawalstatepending)
                 * documentation.
                 */
                data object Pending : RevenueWithdrawalState {
                    override val type: Type = Type.PENDING
                }

                /**
                 * Case when the withdrawal succeeded.
                 *
                 * See Telegram's
                 * [RevenueWithdrawalStateSucceeded](https://core.telegram.org/bots/api#revenuewithdrawalstatesucceeded)
                 * documentation.
                 */
                @TelegramCodegen.Type
                data class Succeeded internal constructor(
                    /** Date the withdrawal was completed. */
                    val date: Instant,
                    /** URL that can be used to see the transaction in the TON blockchain explorer. */
                    val url: String
                ) : RevenueWithdrawalState {
                    override val type: Type = Type.SUCCEEDED

                    companion object
                }

                /**
                 * Case when the withdrawal failed and the transaction was refunded.
                 *
                 * See Telegram's
                 * [RevenueWithdrawalStateFailed](https://core.telegram.org/bots/api#revenuewithdrawalstatefailed)
                 * documentation.
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
         * Case when the transaction partner is Telegram Ads.
         *
         * See Telegram's
         * [Telegram Ads partner](https://core.telegram.org/bots/api#transactionpartnertelegramads) documentation.
         */
        data object TelegramAds : TransactionPartner {
            override val type: Type = Type.TELEGRAM_ADS
        }

        /**
         * Case when the transaction partner is the Telegram API.
         *
         * See Telegram's
         * [TransactionPartnerTelegramApi](https://core.telegram.org/bots/api#transactionpartnertelegramapi)
         * documentation.
         */
        @TelegramCodegen.Type
        data class TelegramApi internal constructor(
            /** Number of successful paid requests. */
            val requestCount: Int
        ) : TransactionPartner {
            override val type: Type = Type.TELEGRAM_API

            companion object
        }

        /**
         * Case when the transaction partner is not covered by another subtype.
         *
         * See Telegram's [TransactionPartnerOther](https://core.telegram.org/bots/api#transactionpartnerother)
         * documentation.
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
