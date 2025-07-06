package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [removeBusinessAccountProfilePhoto](https://core.telegram.org/bots/api#removebusinessaccountprofilephoto) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class RemoveBusinessAccountProfilePhoto internal constructor(
    val businessConnectionId: String,
    @get:JvmName("getIsPublic")
    val isPublic: Boolean = false,
) : JsonTelegramCallable<Boolean>()
