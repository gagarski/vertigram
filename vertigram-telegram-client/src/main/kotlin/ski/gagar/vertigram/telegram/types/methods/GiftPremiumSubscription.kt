package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonValue
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.richtext.RichText

/**
 * Telegram [giftPremiumSubscription](https://core.telegram.org/bots/api#giftpremiumsubscription) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class GiftPremiumSubscription internal constructor(
    val userId: Long,
    val monthCount: MonthCount,
    val text: String? = null,
    val textParseMode: RichText.ParseMode? = null,
    val textEntities: List<MessageEntity>? = null
) : JsonTelegramCallable<Boolean>() {
    val starCount: Int get() = monthCount.starCount

    enum class MonthCount(@JsonValue val count: Int, val starCount: Int) {
        THREE(3, 1000),
        SIX(6, 1500),
        TWELVE(12, 2500)
    }
    companion object
}