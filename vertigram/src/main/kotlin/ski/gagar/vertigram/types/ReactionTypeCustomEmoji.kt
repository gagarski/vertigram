package ski.gagar.vertigram.types

data class ReactionTypeCustomEmoji(val customEmojiId: String) : ReactionType {
    override val type: ReactionTypeType = ReactionTypeType.CUSTOM_EMOJI
}