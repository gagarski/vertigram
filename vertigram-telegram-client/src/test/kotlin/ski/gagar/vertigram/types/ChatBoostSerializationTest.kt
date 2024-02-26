package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

object ChatBoostSerializationTest : BaseSerializationTest() {
    @Test
    fun `chat boost source should survive serialization`() {
        assertSerializable<ChatBoost.Source>(
            ChatBoost.Source.Premium(
                user = User(
                    id = 1
                )
            )
        )
        assertSerializable<ChatBoost.Source>(
            ChatBoost.Source.GiftCode(
                user = User(
                    id = 1
                )
            )
        )
        assertSerializable<ChatBoost.Source>(
            ChatBoost.Source.Giveaway(
                giveAwayMessageId = 1,
                user = User(
                    id = 1
                )
            )
        )

    }

}