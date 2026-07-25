package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.colors.ArgbColor

object StoryAreaSerializationTest : BaseSerializationTest() {
    @Test
    fun `story area type should survive serialization`() {
        assertSerializable<Story.Area.Type>(
            Story.Area.Type.Location.create(
                latitude = 0.0,
                longitude = 0.0,
                address = Story.Area.Type.Location.Address.create(
                    countryCode = "US"
                )
            )
        )
        assertSerializable<Story.Area.Type>(
            Story.Area.Type.SuggestedReaction.create(
                reactionType = Reaction.Emoji.create(
                    emoji = ":D"
                )
            )
        )
        assertSerializable<Story.Area.Type>(
            Story.Area.Type.Link.create(
                url = "https://www"
            )
        )
        assertSerializable<Story.Area.Type>(
            Story.Area.Type.Weather.create(
                temperature = 0.0,
                emoji = ":D",
                backgroundColor = ArgbColor(0U, 0U, 0U, 0U)
            )
        )
        assertSerializable<Story.Area.Type>(
            Story.Area.Type.UniqueGift.create(
                name = "wolf"
            )
        )
    }
}
