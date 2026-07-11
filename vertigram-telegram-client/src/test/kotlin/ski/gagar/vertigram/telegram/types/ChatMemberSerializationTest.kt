package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.util.orEpoch
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER
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

    @Test
    fun `restricted chat member should deserialize from telegram json`() {
        TELEGRAM_JSON_MAPPER.readValue(
            """
                {
                  "status": "restricted",
                  "user": {
                    "id": 1,
                    "is_bot": false,
                    "first_name": "Test"
                  },
                  "is_member": true,
                  "can_send_messages": true,
                  "can_send_audios": true,
                  "can_send_documents": true,
                  "can_send_photos": true,
                  "can_send_videos": true,
                  "can_send_video_notes": true,
                  "can_send_voice_notes": true,
                  "can_send_polls": true,
                  "can_send_other_messages": true,
                  "can_add_web_page_previews": true,
                  "can_change_info": true,
                  "can_invite_users": true,
                  "can_pin_messages": true,
                  "can_manage_topics": true,
                  "until_date": 1
                }
            """.trimIndent(),
            ChatMember::class.java
        )
    }

}
