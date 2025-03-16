package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [ChatBoost](https://core.telegram.org/bots/api#chatboost) type.
 *
 * The related types (e.g. `ChatBoostSource`) are nested and renamed to more concise name.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ChatBoost internal constructor(
    val boostId: String,
    val addDate: Instant,
    val expirationDate: Instant,
    val source: Source
) {
    /**
     * Telegram [ChatBoostAdded](https://core.telegram.org/bots/api#chatboostadded) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Added internal constructor(
        val boostCount: Int
    ) {
        companion object
    }

    /**
     * Telegram [ChatBoostSource](https://core.telegram.org/bots/api#chatboostsource) type.
     *
     * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
     * names given they are nested into [ChatBoost.Source] class. The rule here is the following:
     * `ChatBoostSourceXxx` Telegram type becomes `ChatBoost.Source.Xxx`.
     *
     * For up-to-date documentation please consult the official Telegram docs.
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
         * Telegram [ChatBoostSourceGiftCode](https://core.telegram.org/bots/api#chatboostsourcegiftcode) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class GiftCode internal constructor(
            override val user: User
        ) : Source {
            override val source: Type = Type.GIFT_CODE
            companion object
        }

        /**
         * Telegram [ChatBoostSourceGiveaway](https://core.telegram.org/bots/api#chatboostsourcegiveaway) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Giveaway internal constructor(
            val giveAwayMessageId: Long,
            override val user: User,
            val prizeStarCount: Int? = null,
            @get:JvmName("getIsUnclaimed")
            val isUnclaimed: Boolean = false
        ) : Source {
            override val source: Type = Type.GIVEAWAY
            companion object
        }

        /**
         * Telegram [ChatBoostSourcePremium](https://core.telegram.org/bots/api#chatboostsourcepremium) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Premium internal constructor(
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
