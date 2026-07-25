package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to promote or demote a user in a supergroup or a channel.
 *
 * The bot must be an administrator in the chat for this to work and must have appropriate administrator rights.
 * Pass `false` for all Boolean parameters to demote a user. Returns `true` on success.
 *
 * See Telegram's [promoteChatMember](https://core.telegram.org/bots/api#promotechatmember) documentation.
 */
@TelegramCodegen.Method
data class PromoteChatMember internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long,
    /** Pass `true` if the administrator's presence in the chat is hidden. */
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    /** Pass `true` if the administrator can access the event log, statistics, members and other administrators. */
    val canManageChat: Boolean = false,
    /** Pass `true` if the administrator can delete messages of other users. */
    val canDeleteMessages: Boolean = false,
    /** Pass `true` if the administrator can manage video chats. */
    val canManageVideoChats: Boolean = false,
    /** Pass `true` if the administrator can restrict, ban or unban chat members. */
    val canRestrictMembers: Boolean = false,
    /** Pass `true` if the administrator can add new administrators with a subset of their own privileges. */
    val canPromoteMembers: Boolean = false,
    /** Pass `true` if the administrator can change the chat title, photo and other settings. */
    val canChangeInfo: Boolean = false,
    /** Pass `true` if the administrator can invite new users to the chat. */
    val canInviteUsers: Boolean = false,
    /** Pass `true` if the administrator can post messages in the channel and approve suggested posts. */
    val canPostMessages: Boolean = false,
    /** Pass `true` if the administrator can edit messages of other users and pin messages in channels. */
    val canEditMessages: Boolean = false,
    /** Pass `true` if the administrator can pin messages in supergroups. */
    val canPinMessages: Boolean = false,
    /** Pass `true` if the administrator can post stories to the chat. */
    val canPostStories: Boolean = false,
    /** Pass `true` if the administrator can edit stories posted by other users. */
    val canEditStories: Boolean = false,
    /** Pass `true` if the administrator can delete stories posted by other users. */
    val canDeleteStories: Boolean = false,
    /** Pass `true` if the administrator can create, rename, close and reopen forum topics. */
    val canManageTopics: Boolean = false,
    /** Pass `true` if the administrator can manage direct messages and decline suggested posts. */
    val canManageDirectMessages: Boolean = false,
    /** Pass `true` if the administrator can edit the tags of regular members. */
    val canManageTags: Boolean = false,
) : JsonTelegramCallable<Boolean>(), HasChatId
