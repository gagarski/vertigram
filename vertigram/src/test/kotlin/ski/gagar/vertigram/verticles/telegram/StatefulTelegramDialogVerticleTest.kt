package ski.gagar.vertigram.verticles.telegram

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.attachVertigram
import ski.gagar.vertigram.telegram.markup.toFormattedText
import ski.gagar.vertigram.telegram.types.Chat
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.create
import ski.gagar.vertigram.verticles.telegram.StatefulTelegramDialogVerticle.EphemeralHook
import ski.gagar.vertigram.verticles.telegram.StatefulTelegramDialogVerticle.EphemeralState
import ski.gagar.vertigram.verticles.telegram.StatefulTelegramDialogVerticle.State
import ski.gagar.vertigram.verticles.common.HierarchyVerticle
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason
import java.time.Duration
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

class StatefulTelegramDialogVerticleTest {
    @Test
    fun `ephemeral state installs incoming hook before callback handler`() = runBlocking {
        val verticle = TestDialogVerticle()
        val state = RecordingEphemeralState(verticle)
        val callback = Update.CallbackQuery.Payload.create(
            id = "callback-id",
            from = User.create(id = 2),
            chatInstance = "chat-instance"
        )
        val hook = EphemeralHook.from(callback)

        state.setCurrentEphemeralHook(EphemeralHook.from(message(messageId = 1)))
        state.handleCallbackQuery(callback, hook)

        assertEquals(hook, state.seenHook)
        assertEquals(hook, state.exposedCurrentHook)
    }

    @Test
    fun `temporary ephemeral hook is restored after handler completes`() = runBlocking {
        val state = RecordingEphemeralState(TestDialogVerticle())
        val capturedHook = EphemeralHook.from(message(messageId = 1))
        val latestHook = EphemeralHook.from(message(messageId = 2))
        state.setCurrentEphemeralHook(latestHook)

        state.withCurrentEphemeralHook(capturedHook) {
            assertEquals(capturedHook, state.exposedCurrentHook)
        }

        assertEquals(latestHook, state.exposedCurrentHook)
    }

    @Test
    fun `temporary ephemeral hook is restored after handler fails`() {
        val state = RecordingEphemeralState(TestDialogVerticle())
        val capturedHook = EphemeralHook.from(message(messageId = 1))
        val latestHook = EphemeralHook.from(message(messageId = 2))
        state.setCurrentEphemeralHook(latestHook)

        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                state.withCurrentEphemeralHook(capturedHook) {
                    assertEquals(capturedHook, state.exposedCurrentHook)
                    throw IllegalArgumentException("expected")
                }
            }
        }

        assertEquals(latestHook, state.exposedCurrentHook)
    }

    @Test
    fun `plain become fails outside dialog lock`() {
        val verticle = TestDialogVerticle()
        val source = ExposedRegularState(verticle)
        val target = ExposedRegularState(verticle)

        assertThrows(IllegalStateException::class.java) {
            runBlocking { source.transitionTo(target) }
        }
    }

    @Test
    fun `sendOrEdit fails outside dialog lock`() {
        val state = ExposedRegularState(TestDialogVerticle())

        assertThrows(IllegalStateException::class.java) {
            runBlocking { state.send("test") }
        }
    }

    @Test
    fun `regular message filter does not reject ephemeral messages by default`() = runBlocking {
        val state = FilteringRegularState(TestDialogVerticle())
        val message = message(messageId = 1)

        state.handleEphemeralMessage(message, EphemeralHook.from(message))

        assertTrue(state.ephemeralMessageHandled)
    }

    @Test
    fun `regular state can explicitly reject ephemeral messages`() = runBlocking {
        val state = RejectingEphemeralRegularState(TestDialogVerticle())
        val message = message(messageId = 1)

        state.handleEphemeralMessage(message, EphemeralHook.from(message))

        assertFalse(state.ephemeralMessageHandled)
    }

    @Test
    fun `child death handler can transition while holding dialog lock`() = runBlocking {
        val verticle = TestDialogVerticle()
        val target = ExposedRegularState(verticle)
        val handled = CompletableDeferred<Unit>()
        val source = ExposedRegularState(verticle, childDeathTarget = target, childDeathHandled = handled)
        source.enter()

        verticle.deliverChildDeath()

        assertTrue(handled.isCompleted)
        assertEquals(target, verticle.currentState)
    }

    @Test
    fun `child death waits for dialog lock and is delivered to latest state`() = runBlocking {
        val verticle = TestDialogVerticle()
        val sourceHandled = CompletableDeferred<Unit>()
        val targetHandled = CompletableDeferred<Unit>()
        val source = ExposedRegularState(verticle, childDeathHandled = sourceHandled)
        val target = ExposedRegularState(verticle, childDeathHandled = targetHandled)
        val lockHeld = CompletableDeferred<Unit>()
        val releaseLock = CompletableDeferred<Unit>()
        val deathDispatchStarted = CompletableDeferred<Unit>()
        verticle.childDeathDispatchStarted = deathDispatchStarted
        source.enter()

        val transition = launch {
            source.transitionWhileHoldingLock(target, lockHeld, releaseLock)
        }
        lockHeld.await()
        val childDeath = launch {
            verticle.deliverChildDeath()
        }
        deathDispatchStarted.await()

        assertFalse(sourceHandled.isCompleted)
        assertFalse(targetHandled.isCompleted)

        releaseLock.complete(Unit)
        transition.join()
        childDeath.join()

        assertFalse(sourceHandled.isCompleted)
        assertTrue(targetHandled.isCompleted)
        assertEquals(target, verticle.currentState)
    }

    @Test
    fun `timer waits for dialog lock instead of being discarded`() = runBlocking {
        val vertx = Vertx.vertx()
        val lockHeld = CompletableDeferred<Unit>()
        val releaseLock = CompletableDeferred<Unit>()
        val handled = CompletableDeferred<Unit>()

        try {
            val vertigram = vertx.attachVertigram()
            val deployment = async {
                vertigram.deployVerticle(LockWaitingTimerDialog(lockHeld, releaseLock, handled))
            }
            lockHeld.await()
            delay(100)
            assertFalse(handled.isCompleted)

            releaseLock.complete(Unit)
            deployment.await()
            withTimeout(5.seconds) { handled.await() }
        } finally {
            releaseLock.complete(Unit)
            vertx.close().coAwait()
        }
    }

    @Test
    fun `timer queued before dialog death is discarded`() = runBlocking {
        val vertx = Vertx.vertx()
        val deathNotice = CompletableDeferred<DeathNotice>()
        val handled = CompletableDeferred<Unit>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                TimerParent(QueuedDeathTimerDialog(handled), deathNotice)
            )

            val notice = withTimeout(5.seconds) { deathNotice.await() }
            assertEquals(DeathReason.COMPLETED, notice.reason)
            delay(100)
            assertFalse(handled.isCompleted)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `timer still delaying when dialog dies is discarded`() = runBlocking {
        val vertx = Vertx.vertx()
        val deathNotice = CompletableDeferred<DeathNotice>()
        val handled = CompletableDeferred<Unit>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                TimerParent(DelayedDeathTimerDialog(handled), deathNotice)
            )

            val notice = withTimeout(5.seconds) { deathNotice.await() }
            assertEquals(DeathReason.COMPLETED, notice.reason)
            delay(250)
            assertFalse(handled.isCompleted)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `state timer exception fails dialog`() = runBlocking {
        val notice = runFailingTimerDialog(FailingStateTimerDialog())

        assertEquals(DeathReason.FAILED, notice.reason)
    }

    @Test
    fun `custom timeout state exception fails dialog`() = runBlocking {
        val notice = runFailingTimerDialog(FailingTimeoutDialog())

        assertEquals(DeathReason.FAILED, notice.reason)
    }

    @Test
    fun `rollback APIs fail outside lock without consuming history`() = runBlocking {
        val verticle = TestDialogVerticle()
        val previous = ExposedRegularState(verticle)
        val current = ExposedRegularState(verticle)

        previous.enter()
        current.enter()

        assertThrows(IllegalStateException::class.java) { current.canRollbackUnlocked(null) }
        assertThrows(IllegalStateException::class.java) { current.canRollbackRegularUnlocked() }
        assertThrows(IllegalStateException::class.java) {
            runBlocking { current.rollbackUnlocked(null) }
        }
        assertThrows(IllegalStateException::class.java) {
            runBlocking { current.rollbackRegularUnlocked() }
        }

        assertEquals(current, verticle.currentState)
        assertTrue(current.canRollbackWith(null))
        assertTrue(current.canRollbackRegularly())
    }

    @Test
    fun `rollback without hook restores only an immediate regular state`() = runBlocking {
        val verticle = TestDialogVerticle()
        val first = ExposedRegularState(verticle)
        val current = ExposedRegularState(verticle)

        first.enter()
        current.enter()

        assertTrue(current.canRollbackWith(null))
        current.rollbackWith(null)
        assertEquals(first, verticle.currentState)
    }

    @Test
    fun `rollback without hook preserves immediate ephemeral history`() = runBlocking {
        val verticle = TestDialogVerticle()
        val first = ExposedRegularState(verticle)
        val ephemeral = RecordingEphemeralState(verticle)
        val current = ExposedRegularState(verticle)
        val oldHook = EphemeralHook.from(message(messageId = 1))
        val freshHook = EphemeralHook.from(message(messageId = 2))

        first.enter()
        ephemeral.enter(oldHook)
        current.enter()

        assertFalse(current.canRollbackWith(null))
        current.rollbackWith(null)
        assertEquals(current, verticle.currentState)

        assertTrue(current.canRollbackWith(freshHook))
        current.rollbackWith(freshHook)
        assertEquals(ephemeral, verticle.currentState)
        assertEquals(freshHook, ephemeral.exposedCurrentHook)
    }

    @Test
    fun `regular rollback skips ephemeral entries`() = runBlocking {
        val verticle = TestDialogVerticle()
        val regular = ExposedRegularState(verticle)
        val firstEphemeral = RecordingEphemeralState(verticle)
        val secondEphemeral = RecordingEphemeralState(verticle)
        val current = ExposedRegularState(verticle)
        val hook = EphemeralHook.from(message(messageId = 1))

        regular.enter()
        firstEphemeral.enter(hook)
        secondEphemeral.enter(hook)
        current.enter()

        assertTrue(current.canRollbackRegularly())
        current.rollbackRegularly()
        assertEquals(regular, verticle.currentState)
        assertFalse(regular.canRollbackWith(hook))
    }

    @Test
    fun `regular rollback preserves history when no regular state exists`() = runBlocking {
        val verticle = TestDialogVerticle()
        val firstEphemeral = RecordingEphemeralState(verticle)
        val current = ExposedRegularState(verticle)
        val hook = EphemeralHook.from(message(messageId = 1))

        firstEphemeral.enterAsFirst(hook)
        current.enter()

        assertFalse(current.canRollbackRegularly())
        current.rollbackRegularly()
        assertEquals(current, verticle.currentState)
        assertTrue(current.canRollbackWith(hook))
    }

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

    @Test
    fun `message hook preserves regular and ephemeral message identifiers`() {
        val hook = EphemeralHook.from(message(messageId = 11, ephemeralMessageId = 22))
            as EphemeralHook.Message

        assertEquals(1, hook.user.id)
        assertEquals(1, hook.userId)
        assertEquals(11, hook.messageId)
        assertEquals(22, hook.ephemeralMessageId)
    }

    @Test
    fun `private message hook does not invent an ephemeral identifier`() {
        val hook = EphemeralHook.from(message(messageId = 11)) as EphemeralHook.Message

        assertNull(hook.ephemeralMessageId)
    }

    @Test
    fun `callback hook preserves callback identifier and user`() {
        val callback = Update.CallbackQuery.Payload.create(
            id = "callback-id",
            from = User.create(id = 2),
            chatInstance = "chat-instance"
        )
        val hook = EphemeralHook.from(callback) as EphemeralHook.CallbackQuery

        assertEquals(2, hook.user.id)
        assertEquals(2, hook.userId)
        assertEquals("callback-id", hook.callbackQueryId)
    }

    @Test
    fun `standalone hook preserves target user identifier`() {
        val hook = EphemeralHook.forUser(3)

        assertEquals(3, hook.userId)
        assertTrue(hook is EphemeralHook.Standalone)
    }

    private fun message(messageId: Long, ephemeralMessageId: Long? = null): Message = Message.create(
        messageId = messageId,
        ephemeralMessageId = ephemeralMessageId,
        from = User.create(id = 1),
        date = Instant.EPOCH,
        chat = Chat.create(id = 1, type = Chat.Type.PRIVATE)
    )

    private suspend fun runFailingTimerDialog(
        dialog: StatefulTelegramDialogVerticle<Unit?>
    ): DeathNotice {
        val vertx = Vertx.vertx()
        val deathNotice = CompletableDeferred<DeathNotice>()

        return try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(TimerParent(dialog, deathNotice))
            withTimeout(5.seconds) { deathNotice.await() }
        } finally {
            vertx.close().coAwait()
        }
    }

    private class TestDialogVerticle : StatefulTelegramDialogVerticle<Unit>() {
        override val chatId: Long = 1
        override val initialState: State = ExposedRegularState(this)

        val currentState: AbstractState?
            get() = state

        var childDeathDispatchStarted: CompletableDeferred<Unit>? = null

        suspend fun deliverChildDeath() {
            onChildDeath(DeathNotice("child", DeathReason.COMPLETED))
        }

        override suspend fun onChildDeath(deathNotice: DeathNotice) {
            childDeathDispatchStarted?.complete(Unit)
            super.onChildDeath(deathNotice)
        }
    }

    private class ExposedRegularState(
        verticle: TestDialogVerticle,
        private val childDeathTarget: State? = null,
        private val childDeathHandled: CompletableDeferred<Unit>? = null
    ) : State(verticle) {
        suspend fun transitionTo(state: State) = become(state)
        suspend fun send(text: String) = sendOrEdit(text.toFormattedText())
        suspend fun enter() = withLock { become(this@ExposedRegularState) }
        suspend fun transitionWhileHoldingLock(
            state: State,
            lockHeld: CompletableDeferred<Unit>,
            releaseLock: CompletableDeferred<Unit>
        ) = withLock {
            lockHeld.complete(Unit)
            releaseLock.await()
            become(state)
        }
        suspend fun canRollbackWith(hook: EphemeralHook?): Boolean {
            var result = false
            withLock { result = canRollback(hook) }
            return result
        }
        suspend fun canRollbackRegularly(): Boolean {
            var result = false
            withLock { result = canRollbackRegular() }
            return result
        }
        suspend fun rollbackWith(hook: EphemeralHook?) = withLock { rollback(hook) }
        suspend fun rollbackRegularly() = withLock { rollbackRegular() }
        fun canRollbackUnlocked(hook: EphemeralHook?) = canRollback(hook)
        fun canRollbackRegularUnlocked() = canRollbackRegular()
        suspend fun rollbackUnlocked(hook: EphemeralHook?) = rollback(hook)
        suspend fun rollbackRegularUnlocked() = rollbackRegular()

        override suspend fun onChildDeath(deathNotice: DeathNotice) {
            childDeathHandled?.complete(Unit)
            childDeathTarget?.let { become(it) }
        }
    }

    private class RecordingEphemeralState(
        verticle: TestDialogVerticle
    ) : EphemeralState(verticle) {
        var seenHook: EphemeralHook? = null

        val exposedCurrentHook: EphemeralHook
            get() = ephemeralHook

        suspend fun enter(hook: EphemeralHook) = withLock { become(this@RecordingEphemeralState, hook) }
        suspend fun enterAsFirst(hook: EphemeralHook) = enter(hook)

        override suspend fun doHandleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            seenHook = this.ephemeralHook
        }
    }

    private open class FilteringRegularState(verticle: TestDialogVerticle) : State(verticle) {
        var ephemeralMessageHandled = false

        override suspend fun shouldHandleMessage(message: Message): Boolean = false

        override suspend fun doHandleEphemeralMessage(message: Message, ephemeralHook: EphemeralHook) {
            ephemeralMessageHandled = true
        }
    }

    private class RejectingEphemeralRegularState(
        verticle: TestDialogVerticle
    ) : FilteringRegularState(verticle) {
        override suspend fun shouldHandleEphemeralMessage(
            message: Message,
            ephemeralHook: EphemeralHook
        ): Boolean = false
    }

    private class TimerParent(
        private val child: VertigramVerticle<Unit?>,
        private val deathNotice: CompletableDeferred<DeathNotice>
    ) : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            deployChild(child)
        }

        override suspend fun onChildDeath(deathNotice: DeathNotice) {
            this.deathNotice.complete(deathNotice)
        }
    }

    private class LockWaitingTimerDialog(
        lockHeld: CompletableDeferred<Unit>,
        releaseLock: CompletableDeferred<Unit>,
        handled: CompletableDeferred<Unit>
    ) : StatefulTelegramDialogVerticle<Unit?>() {
        override val chatId: Long = 1
        override val callbackQueryListenAddress: String? = null
        override val messageListenAddress: String? = null
        override val initialState: State = object : State(this) {
            override suspend fun sideEffect() {
                setTimer(Duration.ZERO) { handled.complete(Unit) }
                lockHeld.complete(Unit)
                releaseLock.await()
            }
        }
    }

    private class QueuedDeathTimerDialog(
        handled: CompletableDeferred<Unit>
    ) : StatefulTelegramDialogVerticle<Unit?>() {
        override val chatId: Long = 1
        override val callbackQueryListenAddress: String? = null
        override val messageListenAddress: String? = null
        override val initialState: State = object : State(this) {
            override suspend fun sideEffect() {
                setTimer(Duration.ZERO) { handled.complete(Unit) }
                delay(100)
                complete()
            }
        }
    }

    private class FailingStateTimerDialog : StatefulTelegramDialogVerticle<Unit?>() {
        override val chatId: Long = 1
        override val callbackQueryListenAddress: String? = null
        override val messageListenAddress: String? = null
        override val initialState: State = object : State(this) {
            override suspend fun sideEffect() {
                setTimer(Duration.ZERO) {
                    throw IllegalStateException("Expected state timer failure")
                }
            }
        }
    }

    private class DelayedDeathTimerDialog(
        handled: CompletableDeferred<Unit>
    ) : StatefulTelegramDialogVerticle<Unit?>() {
        override val chatId: Long = 1
        override val callbackQueryListenAddress: String? = null
        override val messageListenAddress: String? = null
        override val initialState: State = object : State(this) {
            override suspend fun sideEffect() {
                setTimer(Duration.ofMillis(100)) { handled.complete(Unit) }
                complete()
            }
        }
    }

    private class FailingTimeoutDialog : StatefulTelegramDialogVerticle<Unit?>() {
        override val chatId: Long = 1
        override val callbackQueryListenAddress: String? = null
        override val messageListenAddress: String? = null
        override val timeout: Duration = Duration.ZERO
        override val initialState: State = object : State(this) {}
        override val timeoutState: State = object : State(this) {
            override suspend fun sideEffect() {
                throw IllegalStateException("Expected timeout state failure")
            }
        }
    }
}
