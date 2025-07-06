package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.colors.ArgbColor

object StoryAreaSerializationTest : BaseSerializationTest() {
    @Test
    fun `story area type should survive serialization`() {
        assertSerializable<StoryArea.Type>(
            StoryArea.Type.Location.create(
                latitude = 0.0,
                longitude = 0.0,
                address = StoryArea.Type.Location.Address.create(
                    countryCode = "US"
                )
            )
        )
        assertSerializable<StoryArea.Type>(
            StoryArea.Type.SuggestedReaction.create(
                reactionType = Reaction.Emoji.create(
                    emoji = ":D"
                )
            )
        )
        assertSerializable<StoryArea.Type>(
            StoryArea.Type.Link.create(
                url = "https://www"
            )
        )
        assertSerializable<StoryArea.Type>(
            StoryArea.Type.Weather.create(
                temperature = 0.0,
                emoji = ":D",
                backgroundColor = ArgbColor(0U, 0U, 0U, 0U)
            )
        )
        assertSerializable<StoryArea.Type>(
            StoryArea.Type.UniqueGift.create(
                name = "wolf"
            )
        )
    }
}