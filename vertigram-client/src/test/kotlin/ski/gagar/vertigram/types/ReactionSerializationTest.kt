package ski.gagar.vertigram.types

import org.junit.jupiter.api.Test

object ReactionSerializationTest : BaseSerializationTest() {
    @Test
    fun `reaction should survive serialization`() {
        assertSerializable<Reaction>(
            Reaction.Emoji(
                emoji = ":*"
            )
        )
        assertSerializable<Reaction>(
            Reaction.CustomEmoji(
                customEmojiId = "1"
            )
        )
    }

}