package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.BaseSerializationTest.Companion.Mappers
import ski.gagar.vertigram.telegram.types.attachments.StringAttachment

object InputMediaSerializationTest : BaseSerializationTest() {
    private val MAPPERS_TO_SKIP = setOf(Mappers.TELEGRAM)

    @Test
    fun `input media should survive serialization`() {
        assertSerializable<InputMedia>(
            InputMedia.Animation.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia>(
            InputMedia.Audio.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia>(
            InputMedia.Document.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia>(
            InputMedia.Photo.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia>(
            InputMedia.Video.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )
    }

    @Test
    fun `profile photo should survive serialization`() {
        assertSerializable<InputMedia.ProfilePhoto>(
            InputMedia.ProfilePhoto.Static.create(
                photo = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.ProfilePhoto>(
            InputMedia.ProfilePhoto.Animated.create(
                animation = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )
    }

    @Test
    fun `story content should survive serialization`() {
        assertSerializable<InputMedia.StoryContent>(
            InputMedia.StoryContent.Photo.create(
                photo = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.StoryContent>(
            InputMedia.StoryContent.Video.create(
                video = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )
    }
}