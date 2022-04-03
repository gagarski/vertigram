package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Telegram type BotCommandScope.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = BotCommandScopeDefault::class, name = BotCommandScopeType.DEFAULT_STR),
    JsonSubTypes.Type(value = BotCommandScopeAllPrivateChats::class, name = BotCommandScopeType.ALL_PRIVATE_CHATS_STR),
    JsonSubTypes.Type(value = BotCommandScopeAllGroupChats::class, name = BotCommandScopeType.ALL_GROUP_CHATS_STR),
    JsonSubTypes.Type(value = BotCommandScopeAllChatAdministrators::class,
        name = BotCommandScopeType.ALL_CHAT_ADMINISTRATORS_STR),
)
sealed class BotCommandScope {
    abstract val type: BotCommandScopeType
}

