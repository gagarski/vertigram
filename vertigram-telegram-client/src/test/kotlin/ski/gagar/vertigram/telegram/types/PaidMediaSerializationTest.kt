package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.*
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

object PaidMediaSerializationTest : BaseSerializationTest() {

    @Test
    fun `paid media should survive serialization`() {
        assertSerializable<PaidMedia>(
            PaidMedia.Preview.create()
        )
        assertSerializable<PaidMedia>(
            PaidMedia.Photo.create(
                photo = listOf()
            )
        )
        assertSerializable<PaidMedia>(
            PaidMedia.Video.create(
                video = Video.create(
                    fileId = "1",
                    fileUniqueId = "1",
                    width = 100,
                    height = 100,
                    duration = Duration.ofSeconds(100)
                )
            )
        )
    }
}