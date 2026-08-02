package ski.gagar.vertigram.verticles.telegram

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.attachVertigram
import ski.gagar.vertigram.awaitRegistration
import ski.gagar.vertigram.telegram.types.Chat
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.create
import ski.gagar.vertigram.verticles.common.HierarchyVerticle
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason
import ski.gagar.vertigram.verticles.telegram.DispatchVerticle.Deployment
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress
import java.time.Instant
import java.time.Duration as JavaDuration
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration.Companion.seconds

class DispatchVerticleTest {
    @Test
    fun `chat dispatch handles messages without a sender`() = runBlocking {
        val received = Channel<Message>(Channel.UNLIMITED)
        val dispatcher = TestDispatchVerticle(prepare = { _, _ ->
            Deployment(RecordingDialogVerticle(received), null)
        })

        withDispatcher(dispatcher) { vertigram, baseAddress ->
            val channelPost = message(
                messageId = 1,
                chatId = 10,
                from = null,
                chatType = Chat.Type.CHANNEL
            )
            sendMessage(vertigram, baseAddress, channelPost)

            assertEquals(channelPost, withTimeout(5.seconds) { received.receive() })
        }
    }

    @Test
    fun `concurrent messages for a new dialog deploy exactly one child`() = runBlocking {
        val prepareStarted = Channel<Unit>(Channel.UNLIMITED)
        val allowPreparation = CompletableDeferred<Unit>()
        val received = Channel<Message>(Channel.UNLIMITED)
        val starts = AtomicInteger()
        val dispatcher = TestDispatchVerticle(prepare = { _, _ ->
            prepareStarted.send(Unit)
            allowPreparation.await()
            Deployment(RecordingDialogVerticle(received, starts = starts), null)
        })

        withDispatcher(dispatcher) { vertigram, baseAddress ->
            sendMessage(vertigram, baseAddress, messageId = 1, chatId = 10)
            sendMessage(vertigram, baseAddress, messageId = 2, chatId = 10)

            withTimeout(5.seconds) {
                prepareStarted.receive()
                prepareStarted.receive()
            }
            allowPreparation.complete(Unit)

            val receivedIds = withTimeout(5.seconds) {
                setOf(received.receive().messageId, received.receive().messageId)
            }
            assertEquals(setOf(1L, 2L), receivedIds)
            assertEquals(1, starts.get())
        }
    }

    @Test
    fun `different dialogs deploy concurrently`() = runBlocking {
        val childStarting = Channel<Long>(Channel.UNLIMITED)
        val allowChildrenToStart = CompletableDeferred<Unit>()
        val received = Channel<Message>(Channel.UNLIMITED)
        val dispatcher = TestDispatchVerticle(prepare = { key, _ ->
            Deployment(
                RecordingDialogVerticle(received) {
                    childStarting.send(key.chatId)
                    allowChildrenToStart.await()
                },
                null
            )
        })

        withDispatcher(dispatcher) { vertigram, baseAddress ->
            sendMessage(vertigram, baseAddress, messageId = 1, chatId = 10)
            sendMessage(vertigram, baseAddress, messageId = 2, chatId = 20)

            val startingChatIds = withTimeout(5.seconds) {
                setOf(childStarting.receive(), childStarting.receive())
            }
            assertEquals(setOf(10L, 20L), startingChatIds)

            allowChildrenToStart.complete(Unit)
            withTimeout(5.seconds) {
                received.receive()
                received.receive()
            }
        }
    }

    @Test
    fun `messages and callbacks arriving during deployment are queued`() = runBlocking {
        val childStarting = CompletableDeferred<Unit>()
        val allowChildToStart = CompletableDeferred<Unit>()
        val callbackAccepted = Channel<Unit>(Channel.UNLIMITED)
        val receivedMessages = Channel<Message>(Channel.UNLIMITED)
        val receivedCallbacks = Channel<Update.CallbackQuery.Payload>(Channel.UNLIMITED)
        val dispatcher = TestDispatchVerticle(
            prepare = { _, _ ->
                Deployment(
                    RecordingDialogVerticle(
                        messages = receivedMessages,
                        callbacks = receivedCallbacks
                    ) {
                        childStarting.complete(Unit)
                        allowChildToStart.await()
                    },
                    null
                )
            },
            callbackAccepted = callbackAccepted
        )

        withDispatcher(dispatcher) { vertigram, baseAddress ->
            val firstMessage = message(messageId = 1, chatId = 10)
            sendMessage(vertigram, baseAddress, firstMessage)
            withTimeout(5.seconds) { childStarting.await() }

            sendMessage(vertigram, baseAddress, messageId = 2, chatId = 10)
            val callback = callbackQuery(firstMessage)
            vertigram.eventBus.send(
                TelegramAddress.dispatchAddress(Update.Type.CALLBACK_QUERY, baseAddress),
                callback
            )
            withTimeout(5.seconds) { callbackAccepted.receive() }

            allowChildToStart.complete(Unit)

            val messageIds = withTimeout(5.seconds) {
                setOf(receivedMessages.receive().messageId, receivedMessages.receive().messageId)
            }
            assertEquals(setOf(1L, 2L), messageIds)
            assertEquals(callback.id, withTimeout(5.seconds) { receivedCallbacks.receive() }.id)
        }
    }

    @Test
    fun `a later message retries after deployment failure`() = runBlocking {
        val attempts = AtomicInteger()
        val failedStart = CompletableDeferred<Unit>()
        val received = Channel<Message>(Channel.UNLIMITED)
        val dispatcher = TestDispatchVerticle(prepare = { _, _ ->
            when (attempts.incrementAndGet()) {
                1 -> Deployment(FailingDialogVerticle(failedStart), null)
                else -> Deployment(RecordingDialogVerticle(received), null)
            }
        })

        withDispatcher(dispatcher) { vertigram, baseAddress ->
            val failure = try {
                vertigram.eventBus.request<Message, Unit>(
                    TelegramAddress.dispatchAddress(Update.Type.MESSAGE, baseAddress),
                    message(messageId = 1, chatId = 10)
                )
                null
            } catch (t: Throwable) {
                t
            }
            assertNotNull(failure)
            withTimeout(5.seconds) { failedStart.await() }

            sendMessage(vertigram, baseAddress, messageId = 2, chatId = 10)

            assertEquals(2L, withTimeout(5.seconds) { received.receive() }.messageId)
            assertEquals(2, attempts.get())
        }
    }

    @Test
    fun `death notice received before deployment completes does not leave a stale dialog`() = runBlocking {
        val attempts = AtomicInteger()
        val deploymentStarted = Channel<String>(Channel.UNLIMITED)
        val allowStartToComplete = CompletableDeferred<Unit>()
        val deathNoticeHandled = Channel<DeathNotice>(Channel.UNLIMITED)
        val received = Channel<Message>(Channel.UNLIMITED)
        val dispatcher = TestDispatchVerticle(
            prepare = { _, _ ->
                when (attempts.incrementAndGet()) {
                    1 -> Deployment(
                        ControlledStartingDialogVerticle(deploymentStarted, allowStartToComplete),
                        null
                    )
                    else -> Deployment(RecordingDialogVerticle(received), null)
                }
            },
            deathNoticeHandled = deathNoticeHandled,
            cleanupPeriod = JavaDuration.ofMillis(10)
        )

        withDispatcher(dispatcher) { vertigram, baseAddress ->
            val firstRequest = async {
                vertigram.eventBus.request<Message, Unit>(
                    TelegramAddress.dispatchAddress(Update.Type.MESSAGE, baseAddress),
                    message(messageId = 1, chatId = 10)
                )
            }
            val childId = withTimeout(5.seconds) { deploymentStarted.receive() }

            vertigram.eventBus.send(
                dispatcher.childDeathNoticeAddress,
                DeathNotice(childId, DeathReason.COMPLETED)
            )
            withTimeout(5.seconds) { deathNoticeHandled.receive() }

            allowStartToComplete.complete(Unit)
            withTimeout(5.seconds) { firstRequest.await() }

            sendMessage(vertigram, baseAddress, messageId = 2, chatId = 10)

            assertEquals(2L, withTimeout(5.seconds) { received.receive() }.messageId)
            assertEquals(2, attempts.get())
        }
    }

    private suspend fun withDispatcher(
        dispatcher: TestDispatchVerticle,
        block: suspend (Vertigram, String) -> Unit
    ) {
        val vertx = Vertx.vertx()
        val baseAddress = "dispatch-test-${UUID.randomUUID()}"

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                dispatcher,
                TestConfig(baseAddress = baseAddress)
            )
            block(vertigram, baseAddress)
        } finally {
            vertx.close().coAwait()
        }
    }

    private fun sendMessage(vertigram: Vertigram, baseAddress: String, messageId: Long, chatId: Long) {
        sendMessage(vertigram, baseAddress, message(messageId, chatId))
    }

    private fun sendMessage(vertigram: Vertigram, baseAddress: String, message: Message) {
        vertigram.eventBus.send(
            TelegramAddress.dispatchAddress(Update.Type.MESSAGE, baseAddress),
            message
        )
    }

    private fun message(
        messageId: Long,
        chatId: Long,
        from: User? = User.create(id = 1),
        chatType: Chat.Type = Chat.Type.PRIVATE
    ): Message = Message.create(
        messageId = messageId,
        from = from,
        date = Instant.EPOCH,
        chat = Chat.create(id = chatId, type = chatType)
    )

    private fun callbackQuery(message: Message): Update.CallbackQuery.Payload = Update.CallbackQuery.Payload.create(
        id = "callback-${message.messageId}",
        from = User.create(id = 1),
        message = message,
        chatInstance = "chat-${message.chat.id}"
    )

    private data class TestConfig(
        override val baseAddress: String,
        override val verticleAddress: String = "unused-telegram-address"
    ) : DispatchVerticle.Config

    private class TestDispatchVerticle(
        private val prepare: suspend (DialogKey, Message) -> Deployment<Unit?>,
        private val callbackAccepted: Channel<Unit>? = null,
        private val deathNoticeHandled: Channel<DeathNotice>? = null,
        private val cleanupPeriod: JavaDuration? = null
    ) : DispatchVerticle.ByChat<TestConfig, Unit?>() {
        override val pendingDeathNoticeCleanupPeriod: JavaDuration
            get() = cleanupPeriod ?: super.pendingDeathNoticeCleanupPeriod

        val childDeathNoticeAddress: String
            get() = HierarchyVerticle.childDeathNoticeAddress(deploymentID)

        override suspend fun prepareChild(dialogKey: DialogKey, msg: Message): Deployment<Unit?> =
            prepare(dialogKey, msg)

        override suspend fun shouldHandleCallbackQuery(q: Update.CallbackQuery.Payload): Boolean {
            callbackAccepted?.send(Unit)
            return true
        }

        override suspend fun onChildDeath(deathNotice: DeathNotice) {
            super.onChildDeath(deathNotice)
            deathNoticeHandled?.send(deathNotice)
        }
    }

    private class RecordingDialogVerticle(
        private val messages: Channel<Message>,
        private val callbacks: Channel<Update.CallbackQuery.Payload>? = null,
        private val starts: AtomicInteger? = null,
        private val beforeConsumers: suspend () -> Unit = {}
    ) : TelegramDialogVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            starts?.incrementAndGet()
            beforeConsumers()
            consumer<Message, Unit>(messageListenAddress!!, function = messages::send).awaitRegistration()
            callbacks?.let {
                consumer<Update.CallbackQuery.Payload, Unit>(
                    callbackQueryListenAddress!!,
                    function = it::send
                ).awaitRegistration()
            }
        }
    }

    private class FailingDialogVerticle(
        private val failedStart: CompletableDeferred<Unit>
    ) : TelegramDialogVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            failedStart.complete(Unit)
            error("Expected deployment failure")
        }
    }

    private class ControlledStartingDialogVerticle(
        private val deploymentStarted: Channel<String>,
        private val allowStartToComplete: CompletableDeferred<Unit>
    ) : TelegramDialogVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            deploymentStarted.send(deploymentID)
            allowStartToComplete.await()
        }
    }
}
