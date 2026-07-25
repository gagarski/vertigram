package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Describes the type of a reaction.
 *
 * See Telegram's [ReactionType](https://core.telegram.org/bots/api#reactiontype) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Reaction.Emoji::class, name = Reaction.Type.EMOJI_STR),
    JsonSubTypes.Type(value = Reaction.CustomEmoji::class, name = Reaction.Type.CUSTOM_EMOJI_STR),
    JsonSubTypes.Type(value = Reaction.Paid::class, name = Reaction.Type.PAID_STR)
)
sealed interface Reaction {
    val type: Type

    /**
     * Case when the reaction is based on an emoji.
     *
     * See Telegram's [ReactionTypeEmoji](https://core.telegram.org/bots/api#reactiontypeemoji) documentation.
     */
    @TelegramCodegen.Type
    data class Emoji internal constructor(
        /** Reaction emoji. */
        val emoji: String
    ) : Reaction {
        override val type: Type = Type.EMOJI
        companion object
    }

    /**
     * Case when the reaction is based on a custom emoji.
     *
     * See Telegram's [ReactionTypeCustomEmoji](https://core.telegram.org/bots/api#reactiontypecustomemoji)
     * documentation.
     */
    @TelegramCodegen.Type
    data class CustomEmoji internal constructor(
        /** Unique identifier of the custom emoji. */
        val customEmojiId: String
    ) : Reaction {
        override val type: Type = Type.CUSTOM_EMOJI
        companion object
    }

    /**
     * Case when the reaction is a paid reaction.
     *
     * See Telegram's [ReactionTypePaid](https://core.telegram.org/bots/api#reactiontypepaid) documentation.
     */
    data object Paid : Reaction {
        override val type: Type = Type.PAID
    }

    enum class Type {
        @JsonProperty(EMOJI_STR)
        EMOJI,
        @JsonProperty(CUSTOM_EMOJI_STR)
        CUSTOM_EMOJI,
        @JsonProperty(PAID_STR)
        PAID;

        companion object {
            const val EMOJI_STR = "emoji"
            const val CUSTOM_EMOJI_STR = "custom_emoji"
            const val PAID_STR = "paid"
        }
    }

}

