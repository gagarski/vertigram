package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageOriginUser::class, name = MessageOriginType.USER_STR),
    JsonSubTypes.Type(value = MessageOriginHiddenUser::class, name = MessageOriginType.HIDDEN_USER_STR),
    JsonSubTypes.Type(value = MessageOriginChat::class, name = MessageOriginType.CHAT_STR),
    JsonSubTypes.Type(value = MessageOriginChannel::class, name = MessageOriginType.CHANNEL_STR)
)
sealed interface MessageOrigin {
    val type: MessageOriginType
}

