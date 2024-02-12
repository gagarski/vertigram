package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [BotCommandScope](https://core.telegram.org/bots/api#botcommandscope) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = BotCommandScope.Default::class, name = BotCommandScope.Type.DEFAULT_STR),
    JsonSubTypes.Type(value = BotCommandScope.AllPrivateChats::class, name = BotCommandScope.Type.ALL_PRIVATE_CHATS_STR),
    JsonSubTypes.Type(value = BotCommandScope.AllGroupChats::class, name = BotCommandScope.Type.ALL_GROUP_CHATS_STR),
    JsonSubTypes.Type(value = BotCommandScope.AllChatAdministrators::class,
        name = BotCommandScope.Type.ALL_CHAT_ADMINISTRATORS_STR),
    JsonSubTypes.Type(value = BotCommandScope.Chat::class, name = BotCommandScope.Type.CHAT_STR),
    JsonSubTypes.Type(value = BotCommandScope.ChatAdministrators::class,
        name = BotCommandScope.Type.CHAT_ADMINISTRATORS_STR),
    JsonSubTypes.Type(value = BotCommandScope.ChatMember::class, name = BotCommandScope.Type.CHAT_MEMBER_STR),
)
sealed interface BotCommandScope {
    val type: Type

    /**
     * Telegram [BotCommandScopeAllChatAdministrators](https://core.telegram.org/bots/api#botcommandscopeallchatadministrators) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object AllChatAdministrators : BotCommandScope {
        override val type: Type = BotCommandScope.Type.ALL_CHAT_ADMINISTRATORS
    }

    /**
     * Telegram [BotCommandScopeAllGroupChats](https://core.telegram.org/bots/api#botcommandscopeallgroupchats) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object AllGroupChats : BotCommandScope {
        override val type: Type = BotCommandScope.Type.ALL_GROUP_CHATS
    }

    /**
     * Telegram [BotCommandScopeAllPrivateChats](https://core.telegram.org/bots/api#botcommandscopeallprivatechats) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object AllPrivateChats : BotCommandScope {
        override val type: Type = BotCommandScope.Type.ALL_PRIVATE_CHATS
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
    ) : BotCommandScope {
        override val type: Type = BotCommandScope.Type.CHAT
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
    ) : BotCommandScope {
        override val type: Type = BotCommandScope.Type.CHAT_ADMINISTRATORS
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
    ) : BotCommandScope {
        override val type: Type = BotCommandScope.Type.CHAT_MEMBER
    }

    /**
     * Telegram [BotCommandScopeDefault](https://core.telegram.org/bots/api#botcommandscopedefault) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object Default : BotCommandScope {
        override val type: Type = BotCommandScope.Type.DEFAULT
    }

    /**
     * A value for [BotCommandScope.type] field.
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

