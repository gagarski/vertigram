package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Sticker
import java.time.Instant

/**
 * Telegram [setUserEmojiStatus](https://core.telegram.org/bots/api#setuseremojistatus) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetUserEmojiStatus internal constructor(
    val userId: Long,
    val emojiStatusCustomEmojiId: String,
    val emojiStatusExpirationDate: Instant? = null
) : JsonTelegramCallable<Boolean>()
