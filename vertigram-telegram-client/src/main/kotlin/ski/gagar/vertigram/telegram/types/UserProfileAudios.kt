package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Contains a list of user profile audio files.
 *
 * See Telegram's [UserProfileAudios](https://core.telegram.org/bots/api#userprofileaudios) documentation.
 */
@TelegramCodegen.Type
data class UserProfileAudios internal constructor(
    /** Total number of profile audio files for the target user. */
    val totalCount: Int,
    /** Requested profile audio files. */
    val audios: List<Audio>
) {
    companion object
}
