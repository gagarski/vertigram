package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [BotCommand](https://core.telegram.org/bots/api#botcommand) type.
 *
 * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
 * names given they are nested into [BotCommand] class. The rule here is the following:
 * `BotCommandXxx` Telegram type becomes `BotCommand.Xxx`.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class BotCommand(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val command: String,
    val description: String
) {
    /**
     * Telegram [BotCommandScope](https://core.telegram.org/bots/api#botcommandscope) type.
     *
     * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
     * names given they are nested into [BotCommand.Scope] class. The rule here is the following:
     * `BotCommandScopeXxx` Telegram type becomes `BotCommand.Scope.Xxx`
     *
     * For up-to-date documentation please consult the official Telegram docs.
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
         * Telegram [BotCommandScopeAllChatAdministrators](https://core.telegram.org/bots/api#botcommandscopeallchatadministrators) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data object AllChatAdministrators : Scope {
            override val type: Type = Type.ALL_CHAT_ADMINISTRATORS
        }

        /**
         * Telegram [BotCommandScopeAllGroupChats](https://core.telegram.org/bots/api#botcommandscopeallgroupchats) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data object AllGroupChats : Scope {
            override val type: Type = Type.ALL_GROUP_CHATS
        }

        /**
         * Telegram [BotCommandScopeAllPrivateChats](https://core.telegram.org/bots/api#botcommandscopeallprivatechats) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data object AllPrivateChats : Scope {
            override val type: Type = Type.ALL_PRIVATE_CHATS
        }

        /**
         * Telegram [BotCommandScopeChat](https://core.telegram.org/bots/api#botcommandscopechat) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Chat(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chatId: ChatId
        ) : Scope {
            override val type: Type = Type.CHAT
        }

        /**
         * Telegram [BotCommandScopeChatAdministrators](https://core.telegram.org/bots/api#botcommandscopechatadministrators) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class ChatAdministrators(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chatId: ChatId
        ) : Scope {
            override val type: Type = Type.CHAT_ADMINISTRATORS
        }

        /**
         * Telegram [BotCommandScopeChatMember](https://core.telegram.org/bots/api#botcommandscopechatmember) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class ChatMember(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chatId: ChatId,
            val userId: Long
        ) : Scope {
            override val type: Type = Type.CHAT_MEMBER
        }

        /**
         * Telegram [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
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


}
