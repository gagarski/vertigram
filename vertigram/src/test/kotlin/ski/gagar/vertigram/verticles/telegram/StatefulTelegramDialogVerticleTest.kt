package ski.gagar.vertigram.verticles.telegram

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StatefulTelegramDialogVerticleTest {
    @Test
    fun `any incoming message disrupts regular delivery`() {
        val target = KnownMessageTarget.Regular(id = 1)

        assertTrue(target.isDisruptedBy(isEphemeralMessage = false, senderId = 10))
        assertTrue(target.isDisruptedBy(isEphemeralMessage = true, senderId = 10))
    }

    @Test
    fun `public message disrupts ephemeral delivery regardless of sender`() {
        val target = KnownMessageTarget.Ephemeral(id = 1, receiverUserId = 10)

        assertTrue(target.isDisruptedBy(isEphemeralMessage = false, senderId = 20))
    }

    @Test
    fun `ephemeral message disrupts delivery to the same user`() {
        val target = KnownMessageTarget.Ephemeral(id = 1, receiverUserId = 10)

        assertTrue(target.isDisruptedBy(isEphemeralMessage = true, senderId = 10))
        assertTrue(target.isDisruptedBy(isEphemeralMessage = true, senderId = null))
    }

    @Test
    fun `ephemeral message from another user does not disrupt delivery`() {
        val target = KnownMessageTarget.Ephemeral(id = 1, receiverUserId = 10)

        assertFalse(target.isDisruptedBy(isEphemeralMessage = true, senderId = 20))
    }
}
