package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [UserProfileAudios](https://core.telegram.org/bots/api#userprofileaudios) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class UserProfileAudios internal constructor(
    val totalCount: Int,
    val audios: List<Audio>
) {
    companion object
}
