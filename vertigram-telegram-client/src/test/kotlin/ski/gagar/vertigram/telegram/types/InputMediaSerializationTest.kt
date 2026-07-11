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

        assertSerializable<InputMedia>(
            InputMedia.LivePhoto.create(
                media = StringAttachment("live-photo-video"),
                photo = StringAttachment("live-photo")
            ),
            skip = MAPPERS_TO_SKIP
        )
    }

    @Test
    fun `input paid media should survive serialization`() {
        assertSerializable<InputMedia.Paid>(
            InputMedia.Paid.LivePhoto.create(
                media = StringAttachment("live-photo-video"),
                photo = StringAttachment("live-photo")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Paid>(
            InputMedia.Paid.Photo.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Paid>(
            InputMedia.Paid.Video.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )
    }

    @Test
    fun `input poll media should survive serialization`() {
        assertSerializable<InputMedia.Poll>(
            InputMedia.Animation.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Poll>(
            InputMedia.Audio.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Poll>(
            InputMedia.Document.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Poll>(
            InputMedia.LivePhoto.create(
                media = StringAttachment("live-photo-video"),
                photo = StringAttachment("live-photo")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Poll>(
            InputMedia.Location.create(
                latitude = 1.0,
                longitude = 2.0
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Poll>(
            InputMedia.Photo.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Poll>(
            InputMedia.Venue.create(
                latitude = 1.0,
                longitude = 2.0,
                title = "title",
                address = "address"
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.Poll>(
            InputMedia.Video.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )
    }

    @Test
    fun `input poll option media should survive serialization`() {
        assertSerializable<InputMedia.PollOption>(
            InputMedia.Animation.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.PollOption>(
            InputMedia.LivePhoto.create(
                media = StringAttachment("live-photo-video"),
                photo = StringAttachment("live-photo")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.PollOption>(
            InputMedia.Link.create(
                url = "https://example.com"
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.PollOption>(
            InputMedia.Location.create(
                latitude = 1.0,
                longitude = 2.0
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.PollOption>(
            InputMedia.Photo.create(
                media = StringAttachment("https://www")
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.PollOption>(
            InputMedia.PollSticker.create(
                media = StringAttachment("https://www"),
                emoji = ":)"
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.PollOption>(
            InputMedia.Venue.create(
                latitude = 1.0,
                longitude = 2.0,
                title = "title",
                address = "address"
            ),
            skip = MAPPERS_TO_SKIP
        )

        assertSerializable<InputMedia.PollOption>(
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
