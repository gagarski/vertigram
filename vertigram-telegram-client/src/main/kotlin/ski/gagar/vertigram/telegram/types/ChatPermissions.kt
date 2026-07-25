package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Describes actions that a non-administrator user is allowed to take in a chat.
 *
 * See Telegram's [ChatPermissions](https://core.telegram.org/bots/api#chatpermissions) documentation.
 */
@TelegramCodegen.Type
data class ChatPermissions internal constructor(
    /**
     * `true` if the user is allowed to send text messages, rich messages, contacts, giveaways, giveaway winners,
     * invoices, locations and venues.
     */
    val canSendMessages: Boolean = false,
    /** `true` if the user is allowed to send audios. */
    val canSendAudios: Boolean = false,
    /** `true` if the user is allowed to send documents. */
    val canSendDocuments: Boolean = false,
    /** `true` if the user is allowed to send photos. */
    val canSendPhotos: Boolean = false,
    /** `true` if the user is allowed to send videos. */
    val canSendVideos: Boolean = false,
    /** `true` if the user is allowed to send video notes. */
    val canSendVideoNotes: Boolean = false,
    /** `true` if the user is allowed to send voice notes. */
    val canSendVoiceNotes: Boolean = false,
    /** `true` if the user is allowed to send polls and checklists. */
    val canSendPolls: Boolean = false,
    /** `true` if the user is allowed to send animations, games, stickers and use inline bots. */
    val canSendOtherMessages: Boolean = false,
    /** `true` if the user is allowed to add web page previews to their messages. */
    val canAddWebPagePreviews: Boolean = false,
    /** `true` if the user is allowed to react to messages. */
    val canReactToMessages: Boolean = false,
    /** `true` if the user is allowed to edit their own tag. */
    val canEditTag: Boolean = false,
    /**
     * `true` if the user is allowed to change the chat title, photo and other settings. Ignored in public supergroups.
     */
    val canChangeInfo: Boolean = false,
    /** `true` if the user is allowed to invite new users to the chat. */
    val canInviteUsers: Boolean = false,
    /** `true` if the user is allowed to pin messages. Ignored in public supergroups. */
    val canPinMessages: Boolean = false,
    /** `true` if the user is allowed to create forum topics. */
    val canManageTopics: Boolean = false
) {
    companion object
}
