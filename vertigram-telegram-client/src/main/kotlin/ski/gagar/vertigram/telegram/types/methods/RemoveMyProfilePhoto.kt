package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Removes the current profile photo of the bot. Returns `true` on success.
 *
 * See Telegram's [removeMyProfilePhoto](https://core.telegram.org/bots/api#removemyprofilephoto) documentation.
 */
@TelegramCodegen.Method
data object RemoveMyProfilePhoto : JsonTelegramCallable<Boolean>()
