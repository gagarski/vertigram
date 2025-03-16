package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.util.orEpoch
import java.time.Instant
import java.time.temporal.ChronoUnit

object ChatMemberSerializationTest : BaseSerializationTest() {
    @Test
    fun `chat member source should survive serialization`() {
        assertSerializable<ChatMember>(
            ChatMember.Owner.create(
                user = User.create(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Administrator.create(
                user = User.create(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Member.create(
                user = User.create(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Restricted.create(
                user = User.create(
                    id = 1
                ),
                untilDate = Instant.now().orEpoch().truncatedTo(ChronoUnit.SECONDS)
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Left.create(
                user = User.create(
                    id = 1
                )
            )
        )
        assertSerializable<ChatMember>(
            ChatMember.Banned.create(
                user = User.create(
                    id = 1
                ),
                untilDate = Instant.now().orEpoch().truncatedTo(ChronoUnit.SECONDS)
            )
        )
    }

}