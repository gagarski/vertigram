package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * This object contains information about a chat boost.
 *
 * See Telegram's [ChatBoost](https://core.telegram.org/bots/api#chatboost) documentation.
 */
@TelegramCodegen.Type
data class ChatBoost internal constructor(
    /** Unique identifier of the boost. */
    val boostId: String,
    /** Point in time when the chat was boosted. */
    val addDate: Instant,
    /**
     * Point in time when the boost will automatically expire, unless the booster's Telegram Premium subscription is
     * prolonged.
     */
    val expirationDate: Instant,
    /** Source of the added boost. */
    val source: Source
) {
    /**
     * This object represents a service message about a user boosting a chat.
     *
     * See Telegram's [ChatBoostAdded](https://core.telegram.org/bots/api#chatboostadded) documentation.
     */
    @TelegramCodegen.Type
    data class Added internal constructor(
        /** Number of boosts added by the user. */
        val boostCount: Int
    ) {
        companion object
    }

    /**
     * This object describes the source of a chat boost.
     *
     * See Telegram's [ChatBoostSource](https://core.telegram.org/bots/api#chatboostsource) documentation.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "source", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Source.Premium::class, name = ChatBoost.Source.Type.PREMIUM_STR),
        JsonSubTypes.Type(value = Source.GiftCode::class, name = ChatBoost.Source.Type.GIFT_CODE_STR),
        JsonSubTypes.Type(value = Source.Giveaway::class, name = ChatBoost.Source.Type.GIVEAWAY_STR)
    )
    sealed interface Source {
        val source: Type
        val user: User
        /**
         * The boost was obtained by the creation of Telegram Premium gift codes to boost a chat. Each such code boosts
         * the chat 4 times for the duration of the corresponding Telegram Premium subscription.
         *
         * See Telegram's [ChatBoostSourceGiftCode](https://core.telegram.org/bots/api#chatboostsourcegiftcode)
         * documentation.
         */
        @TelegramCodegen.Type
        data class GiftCode internal constructor(
            /** User for which the gift code was created. */
            override val user: User
        ) : Source {
            override val source: Type = Type.GIFT_CODE
            companion object
        }

        /**
         * The boost was obtained by the creation of a Telegram Premium or a Telegram Star giveaway. This boosts the
         * chat 4 times for the duration of the corresponding Telegram Premium subscription for Telegram Premium
         * giveaways and [prizeStarCount] / 500 times for one year for Telegram Star giveaways.
         *
         * See Telegram's [ChatBoostSourceGiveaway](https://core.telegram.org/bots/api#chatboostsourcegiveaway)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Giveaway internal constructor(
            /**
             * Identifier of a message in the chat with the giveaway; the message could have been deleted already. May
             * be 0 if the message isn't sent yet.
             */
            val giveAwayMessageId: Long,
            /** User that won the prize in the giveaway, if any; for Telegram Premium giveaways only. */
            override val user: User,
            /** The number of Telegram Stars to be split between giveaway winners; for Telegram Star giveaways only. */
            val prizeStarCount: Int? = null,
            /** `true` if the giveaway was completed, but there was no user to win the prize. */
            @get:JvmName("getIsUnclaimed")
            val isUnclaimed: Boolean = false
        ) : Source {
            override val source: Type = Type.GIVEAWAY
            companion object
        }

        /**
         * The boost was obtained by subscribing to Telegram Premium or by gifting a Telegram Premium subscription to
         * another user.
         *
         * See Telegram's [ChatBoostSourcePremium](https://core.telegram.org/bots/api#chatboostsourcepremium)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Premium internal constructor(
            /** User that boosted the chat. */
            override val user: User
        ) : Source {
            override val source: Type = Type.PREMIUM
            companion object
        }

        /**
         * A value for [ChatBoost.Source.source] field.
         */
        enum class Type {
            @JsonProperty(PREMIUM_STR)
            PREMIUM,
            @JsonProperty(GIFT_CODE_STR)
            GIFT_CODE,
            @JsonProperty(GIVEAWAY_STR)
            GIVEAWAY;

            companion object {
                const val PREMIUM_STR = "premium"
                const val GIFT_CODE_STR = "gift_code"
                const val GIVEAWAY_STR = "giveaway"
            }
        }

    }

    companion object
}
