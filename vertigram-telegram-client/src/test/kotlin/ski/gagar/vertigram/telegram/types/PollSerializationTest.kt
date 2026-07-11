package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import java.time.Duration

object PollSerializationTest : BaseSerializationTest() {
    @Test
    fun `poll should survive serialization`() {
        assertSerializable<Poll>(
            Poll.Regular.create(
                id = "1",
                question = "a",
                options = listOf(),
                totalVoterCount = 1,
                media = Poll.Media.create(
                    livePhoto = LivePhoto.create(
                        fileId = "1",
                        fileUniqueId = "1",
                        width = 100,
                        height = 100,
                        duration = Duration.ofSeconds(10)
                    )
                )
            )
        )
        assertSerializable<Poll>(
            Poll.Quiz.create(
                id = "1",
                question = "a",
                options = listOf(),
                totalVoterCount = 1,
                explanationMedia = Poll.Media.create(
                    video = Video.create(
                        fileId = "1",
                        fileUniqueId = "1",
                        width = 100,
                        height = 100,
                        duration = Duration.ofSeconds(10)
                    )
                )
            )
        )
    }

}
