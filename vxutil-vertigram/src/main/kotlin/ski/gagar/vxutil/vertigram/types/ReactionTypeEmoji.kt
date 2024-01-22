package ski.gagar.vxutil.vertigram.types

data class ReactionTypeEmoji(val emoji: String) : ReactionType {
    override val type: ReactionTypeType = ReactionTypeType.EMOJI
}