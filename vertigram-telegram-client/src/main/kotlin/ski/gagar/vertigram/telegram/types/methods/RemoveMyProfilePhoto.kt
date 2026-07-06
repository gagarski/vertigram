package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [removeMyProfilePhoto](https://core.telegram.org/bots/api#removemyprofilephoto) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data object RemoveMyProfilePhoto : JsonTelegramCallable<Boolean>()
