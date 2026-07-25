package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.richtext.RichText

/**
 * Gifts a Telegram Premium subscription to the given user.
 *
 * Returns `true` on success.
 *
 * See Telegram's
 * [giftPremiumSubscription](https://core.telegram.org/bots/api#giftpremiumsubscription) documentation.
 */
@TelegramCodegen.Method()
data class GiftPremiumSubscription internal constructor(
    /** Unique identifier of the user who will receive the subscription. */
    val userId: Long,
    /** Number of months the Telegram Premium subscription will be active. */
    val monthCount: MonthCount,
    /** Text shown along with the gift, 0-128 characters. */
    val text: String? = null,
    /** Mode for parsing entities in [text]. */
    val textParseMode: RichText.ParseMode? = null,
    /** Special entities that appear in [text]; can be specified instead of [textParseMode]. */
    val textEntities: List<MessageEntity>? = null
) : JsonTelegramCallable<Boolean>() {
    @get:JsonProperty(value = "star_count", access = JsonProperty.Access.READ_ONLY)
    val starCount: Int get() = monthCount.starCount

    /** Supported subscription durations and their Telegram Star prices. */
    enum class MonthCount(@JsonValue val count: Int, val starCount: Int) {
        THREE(3, 1000),
        SIX(6, 1500),
        TWELVE(12, 2500)
    }
    companion object
}
