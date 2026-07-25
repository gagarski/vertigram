package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Sticker
import java.time.Instant

/**
 * Changes the emoji status for a given user who previously allowed the bot to manage their emoji status.
 *
 * Returns `true` on success.
 *
 * See Telegram's [setUserEmojiStatus](https://core.telegram.org/bots/api#setuseremojistatus) documentation.
 */
@TelegramCodegen.Method
data class SetUserEmojiStatus internal constructor(
    /** Unique identifier of the target user. */
    val userId: Long,
    /** Custom emoji identifier of the emoji status. */
    val emojiStatusCustomEmojiId: String,
    /** Expiration date of the emoji status. */
    val emojiStatusExpirationDate: Instant? = null
) : JsonTelegramCallable<Boolean>()
