package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

object ReactionSerializationTest : BaseSerializationTest() {
    @Test
    fun `reaction should survive serialization`() {
        assertSerializable<Reaction>(
            Reaction.Emoji.create(
                emoji = ":*"
            )
        )
        assertSerializable<Reaction>(
            Reaction.CustomEmoji.create(
                customEmojiId = "1"
            )
        )
        assertSerializable<Reaction>(
            Reaction.Paid
        )
    }

}