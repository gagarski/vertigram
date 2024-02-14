package ski.gagar.vertigram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.types.util.orEpoch
import java.time.Instant
import java.time.temporal.ChronoUnit

object ChatMemberSerializationTest : BaseSerializationTest() {
    @Test
    fun `chat member source should survive serialization`() {
        assertSerializable<ChatMember>(
            ChatMember.Owner(
                user = User(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Administrator(
                user = User(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Member(
                user = User(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Restricted(
                user = User(
                    id = 1
                ),
                untilDate = Instant.now().orEpoch().truncatedTo(ChronoUnit.SECONDS)
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Left(
                user = User(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Banned(
                user = User(
                    id = 1
                ),
                untilDate = Instant.now().orEpoch().truncatedTo(ChronoUnit.SECONDS)
            )
        )
    }

}