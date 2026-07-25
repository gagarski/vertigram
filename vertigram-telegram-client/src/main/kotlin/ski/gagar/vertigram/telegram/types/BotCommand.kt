package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents a bot command.
 *
 * See Telegram's [BotCommand](https://core.telegram.org/bots/api#botcommand) documentation.
 */
@TelegramCodegen.Type
data class BotCommand internal constructor(
    /** Text of the command; 1-32 characters. Can contain only lowercase English letters, digits and underscores. */
    val command: String,
    /** Description of the command; 1-256 characters. */
    val description: String,
    /** `true` if the command sends an ephemeral message, which can be seen only by the sender and the bot. */
    @get:JvmName("getIsEphemeral")
    val isEphemeral: Boolean = false
) {
    /**
     * This object represents the scope to which bot commands are applied.
     *
     * See Telegram's [BotCommandScope](https://core.telegram.org/bots/api#botcommandscope) documentation.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Scope.Default::class, name = Scope.Type.DEFAULT_STR),
        JsonSubTypes.Type(value = Scope.AllPrivateChats::class, name = Scope.Type.ALL_PRIVATE_CHATS_STR),
        JsonSubTypes.Type(value = Scope.AllGroupChats::class, name = Scope.Type.ALL_GROUP_CHATS_STR),
        JsonSubTypes.Type(value = Scope.AllChatAdministrators::class,
            name = Scope.Type.ALL_CHAT_ADMINISTRATORS_STR),
        JsonSubTypes.Type(value = Scope.Chat::class, name = Scope.Type.CHAT_STR),
        JsonSubTypes.Type(value = Scope.ChatAdministrators::class,
            name = Scope.Type.CHAT_ADMINISTRATORS_STR),
        JsonSubTypes.Type(value = Scope.ChatMember::class, name = Scope.Type.CHAT_MEMBER_STR),
    )
    sealed interface Scope {
        val type: Type

        /**
         * Represents the [scope][Scope] of bot commands, covering all group and supergroup chat administrators.
         *
         * See Telegram's
         * [BotCommandScopeAllChatAdministrators](https://core.telegram.org/bots/api#botcommandscopeallchatadministrators)
         * documentation.
         */
        data object AllChatAdministrators : Scope {
            override val type: Type = Type.ALL_CHAT_ADMINISTRATORS
        }

        /**
         * Represents the [scope][Scope] of bot commands, covering all group and supergroup chats.
         *
         * See Telegram's
         * [BotCommandScopeAllGroupChats](https://core.telegram.org/bots/api#botcommandscopeallgroupchats)
         * documentation.
         */
        data object AllGroupChats : Scope {
            override val type: Type = Type.ALL_GROUP_CHATS
        }

        /**
         * Represents the [scope][Scope] of bot commands, covering all private chats.
         *
         * See Telegram's
         * [BotCommandScopeAllPrivateChats](https://core.telegram.org/bots/api#botcommandscopeallprivatechats)
         * documentation.
         */
        data object AllPrivateChats : Scope {
            override val type: Type = Type.ALL_PRIVATE_CHATS
        }

        /**
         * Represents the [scope][Scope] of bot commands, covering a specific chat.
         *
         * See Telegram's [BotCommandScopeChat](https://core.telegram.org/bots/api#botcommandscopechat) documentation.
         */
        @TelegramCodegen.Type
        data class Chat internal constructor(
            /**
             * Unique identifier for the target chat or username of the target supergroup. Channel direct messages
             * chats and channel chats aren't supported.
             */
            val chatId: ChatId
        ) : Scope {
            override val type: Type = Type.CHAT
            companion object
        }

        /**
         * Represents the [scope][Scope] of bot commands, covering all administrators of a specific group or supergroup
         * chat.
         *
         * See Telegram's
         * [BotCommandScopeChatAdministrators](https://core.telegram.org/bots/api#botcommandscopechatadministrators)
         * documentation.
         */
        @TelegramCodegen.Type
        data class ChatAdministrators internal constructor(
            /**
             * Unique identifier for the target chat or username of the target supergroup. Channel direct messages
             * chats and channel chats aren't supported.
             */
            val chatId: ChatId
        ) : Scope {
            override val type: Type = Type.CHAT_ADMINISTRATORS
            companion object
        }

        /**
         * Represents the [scope][Scope] of bot commands, covering a specific member of a group or supergroup chat.
         *
         * See Telegram's [BotCommandScopeChatMember](https://core.telegram.org/bots/api#botcommandscopechatmember)
         * documentation.
         */
        @TelegramCodegen.Type
        data class ChatMember internal constructor(
            /**
             * Unique identifier for the target chat or username of the target supergroup. Channel direct messages
             * chats and channel chats aren't supported.
             */
            val chatId: ChatId,
            /** Unique identifier of the target user. */
            val userId: Long
        ) : Scope {
            override val type: Type = Type.CHAT_MEMBER
            companion object
        }

        /**
         * Represents the default [scope][Scope] of bot commands. Default commands are used if no commands with a
         * narrower scope are specified for the user.
         *
         * See Telegram's [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault)
         * documentation.
         */
        data object Default : Scope {
            override val type: Type = Type.DEFAULT
        }

        /**
         * A value for [BotCommand.Scope.type] field.
         */
        enum class Type {
            @JsonProperty(DEFAULT_STR)
            DEFAULT,
            @JsonProperty(ALL_PRIVATE_CHATS_STR)
            ALL_PRIVATE_CHATS,
            @JsonProperty(ALL_GROUP_CHATS_STR)
            ALL_GROUP_CHATS,
            @JsonProperty(ALL_CHAT_ADMINISTRATORS_STR)
            ALL_CHAT_ADMINISTRATORS,
            @JsonProperty(CHAT_STR)
            CHAT,
            @JsonProperty(CHAT_ADMINISTRATORS_STR)
            CHAT_ADMINISTRATORS,
            @JsonProperty(CHAT_MEMBER_STR)
            CHAT_MEMBER;

            companion object {
                const val DEFAULT_STR = "default"
                const val ALL_PRIVATE_CHATS_STR = "all_private_chats"
                const val ALL_GROUP_CHATS_STR = "all_group_chats"
                const val ALL_CHAT_ADMINISTRATORS_STR = "all_chat_administrators"
                const val CHAT_STR = "chat"
                const val CHAT_ADMINISTRATORS_STR = "chat_administrators"
                const val CHAT_MEMBER_STR = "chat_member"
            }
        }
    }

    companion object
}
