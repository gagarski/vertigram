package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

object ChatBoostSerializationTest : BaseSerializationTest() {
    @Test
    fun `chat boost source should survive serialization`() {
        assertSerializable<ChatBoost.Source>(
            ChatBoost.Source.Premium.create(
                user = User.create(
                    id = 1
                )
            )
        )
        assertSerializable<ChatBoost.Source>(
            ChatBoost.Source.GiftCode.create(
                user = User.create(
                    id = 1
                )
            )
        )
        assertSerializable<ChatBoost.Source>(
            ChatBoost.Source.Giveaway.create(
                giveAwayMessageId = 1,
                user = User.create(
                    id = 1
                )
            )
        )

    }

}