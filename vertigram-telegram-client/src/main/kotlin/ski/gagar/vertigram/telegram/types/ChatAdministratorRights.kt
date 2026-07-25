package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents the rights of an administrator in a chat.
 *
 * See Telegram's [ChatAdministratorRights](https://core.telegram.org/bots/api#chatadministratorrights) documentation.
 */
@TelegramCodegen.Type
data class ChatAdministratorRights internal constructor(
    /** `true` if the user's presence in the chat is hidden. */
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    /**
     * `true` if the administrator can access the chat event log, get the boost list, see hidden supergroup and channel
     * members, report spam messages, ignore slow mode, and send messages to the chat without paying Telegram Stars.
     * Implied by any other administrator privilege.
     */
    val canManageChat: Boolean = false,
    /** `true` if the administrator can delete messages of other users. */
    val canDeleteMessages: Boolean = false,
    /** `true` if the administrator can manage video chats. */
    val canManageVideoChats: Boolean = false,
    /** `true` if the administrator can restrict, ban or unban chat members, or access supergroup statistics. */
    val canRestrictMembers: Boolean = false,
    /**
     * `true` if the administrator can add new administrators with a subset of their own privileges or demote
     * administrators that they have promoted, directly or indirectly.
     */
    val canPromoteMembers: Boolean = false,
    /** `true` if the user is allowed to change the chat title, photo and other settings. */
    val canChangeInfo: Boolean = false,
    /** `true` if the user is allowed to invite new users to the chat. */
    val canInviteUsers: Boolean = false,
    /**
     * `true` if the administrator can post messages in the channel, approve suggested posts, or access channel
     * statistics; for channels only.
     */
    val canPostMessages: Boolean = false,
    /** `true` if the administrator can edit messages of other users and can pin messages; for channels only. */
    val canEditMessages: Boolean = false,
    /** `true` if the user is allowed to pin messages; for groups and supergroups only. */
    val canPinMessages: Boolean = false,
    /** `true` if the administrator can post stories to the chat. */
    val canPostStories: Boolean = false,
    /**
     * `true` if the administrator can edit stories posted by other users, post stories to the chat page, pin chat
     * stories, and access the chat's story archive.
     */
    val canEditStories: Boolean = false,
    /** `true` if the administrator can delete stories posted by other users. */
    val canDeleteStories: Boolean = false,
    /** `true` if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only. */
    val canManageTopics: Boolean = false,
    /**
     * `true` if the administrator can manage direct messages of the channel and decline suggested posts; for channels
     * only.
     */
    val canManageDirectMessages: Boolean = false,
    /** `true` if the administrator can edit the tags of regular members; for groups and supergroups only. */
    val canManageTags: Boolean = false,
) {
    companion object
}
