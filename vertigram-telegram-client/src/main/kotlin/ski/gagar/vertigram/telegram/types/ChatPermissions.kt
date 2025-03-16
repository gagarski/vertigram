package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ChatPermissions](https://core.telegram.org/bots/api#chatpermissions) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ChatPermissions internal constructor(
    val canSendMessages: Boolean = false,
    val canSendAudios: Boolean = false,
    val canSendDocuments: Boolean = false,
    val canSendPhotos: Boolean = false,
    val canSendVideos: Boolean = false,
    val canSendVideoNotes: Boolean = false,
    val canSendVoiceNotes: Boolean = false,
    val canSendPolls: Boolean = false,
    val canSendOtherMessages: Boolean = false,
    val canAddWebPagePreviews: Boolean = false,
    val canChangeInfo: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canPinMessages: Boolean = false,
    val canManageTopics: Boolean = false
) {
    companion object
}
