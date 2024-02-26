package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

object PollSerializationTest : BaseSerializationTest() {
    @Test
    fun `poll should survive serialization`() {
        assertSerializable<Poll>(
            Poll.Regular(
                id = "1",
                question = "a",
                options = listOf(),
                totalVoterCount = 1
            )
        )
        assertSerializable<Poll>(
            Poll.Quiz(
                id = "1",
                question = "a",
                options = listOf(),
                totalVoterCount = 1
            )
        )
    }

}