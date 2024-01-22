package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ReactionTypeEmoji::class, name = ReactionTypeType.EMOJI_STR),
    JsonSubTypes.Type(value = ReactionTypeCustomEmoji::class, name = ReactionTypeType.CUSTOM_EMOJI_STR)
)
sealed interface ReactionType {
    val type: ReactionTypeType
}

