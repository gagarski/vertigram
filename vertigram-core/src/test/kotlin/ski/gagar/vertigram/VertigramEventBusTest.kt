package ski.gagar.vertigram

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.util.exceptions.VertigramException
import ski.gagar.vertigram.util.exceptions.VertigramInternalException
import java.time.Instant
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class VertigramEventBusTest {
    @Test
    fun `request and reply preserve nested generic payloads and Java time values`() = runBlocking {
        withVertigram { vertigram, scope ->
            val address = uniqueAddress()
            val request = listOf(
                TestPayload(UUID.randomUUID(), Instant.parse("2026-07-26T12:34:56Z"), "first"),
                TestPayload(UUID.randomUUID(), Instant.parse("2026-07-26T12:35:56Z"), "second")
            )

            vertigram.eventBus.consumer<List<TestPayload>, Map<String, List<TestPayload>>>(
                coroScope = scope,
                address = address
            ) {
                mapOf("echo" to it)
            }.awaitRegistration()

            val reply = vertigram.eventBus.request<List<TestPayload>, Map<String, List<TestPayload>>>(
                address,
                request
            )

            assertEquals(mapOf("echo" to request), reply)
        }
    }

    @Test
    fun `nullable request and reply payloads round trip`() = runBlocking {
        withVertigram { vertigram, scope ->
            val address = uniqueAddress()

            vertigram.eventBus.consumer<TestPayload?, TestPayload?>(
                coroScope = scope,
                address = address
            ) {
                it
            }.awaitRegistration()

            assertNull(
                vertigram.eventBus.request<TestPayload?, TestPayload?>(
                    address,
                    null
                )
            )
        }
    }

    @Test
    fun `publish broadcasts while send delivers a single typed message`() = runBlocking {
        withVertigram { vertigram, scope ->
            val publishAddress = uniqueAddress()
            val sendAddress = uniqueAddress()
            val firstPublished = CompletableDeferred<TestPayload>()
            val secondPublished = CompletableDeferred<TestPayload>()
            val sent = Channel<TestPayload>(Channel.UNLIMITED)

            vertigram.eventBus.consumer<TestPayload, Unit>(scope, publishAddress) {
                firstPublished.complete(it)
            }.awaitRegistration()
            vertigram.eventBus.consumer<TestPayload, Unit>(scope, publishAddress) {
                secondPublished.complete(it)
            }.awaitRegistration()
            vertigram.eventBus.consumer<TestPayload, Unit>(scope, sendAddress) {
                sent.send(it)
            }.awaitRegistration()
            vertigram.eventBus.consumer<TestPayload, Unit>(scope, sendAddress) {
                sent.send(it)
            }.awaitRegistration()

            val published = testPayload("published")
            vertigram.eventBus.publish(publishAddress, published)

            assertEquals(published, withTimeout(5.seconds) { firstPublished.await() })
            assertEquals(published, withTimeout(5.seconds) { secondPublished.await() })

            val single = testPayload("sent")
            vertigram.eventBus.send(sendAddress, single)

            assertEquals(single, withTimeout(5.seconds) { sent.receive() })
            assertNull(withTimeoutOrNull(200.milliseconds) { sent.receive() })
        }
    }

    @Test
    fun `local consumer uses the same Jackson request reply protocol`() = runBlocking {
        withVertigram { vertigram, scope ->
            val address = uniqueAddress()
            val payload = testPayload("local")

            vertigram.eventBus.localConsumer<TestPayload, TestPayload>(
                coroScope = scope,
                address = address
            ) {
                it.copy(label = "${it.label}-reply")
            }.awaitRegistration()

            assertEquals(
                payload.copy(label = "local-reply"),
                vertigram.eventBus.request<TestPayload, TestPayload>(address, payload)
            )
        }
    }

    @Test
    fun `Vertigram names isolate identical logical addresses`() = runBlocking {
        val vertx = Vertx.vertx()
        val scope = vertx.testScope()
        try {
            val first = vertx.attachVertigram(Vertigram.Config(name = "first"))
            val second = vertx.attachVertigram(Vertigram.Config(name = "second"))
            val address = uniqueAddress()
            val firstMessage = CompletableDeferred<TestPayload>()
            val secondMessage = CompletableDeferred<TestPayload>()

            first.eventBus.consumer<TestPayload, Unit>(scope, address) {
                firstMessage.complete(it)
            }.awaitRegistration()
            second.eventBus.consumer<TestPayload, Unit>(scope, address) {
                secondMessage.complete(it)
            }.awaitRegistration()

            val payload = testPayload("first-only")
            first.eventBus.publish(address, payload)

            assertEquals(payload, withTimeout(5.seconds) { firstMessage.await() })
            assertNull(withTimeoutOrNull(200.milliseconds) { secondMessage.await() })
        } finally {
            scope.cancel()
            vertx.close().coAwait()
        }
    }

    @Test
    fun `request deserialization failure produces a hidden internal-error reply`() = runBlocking {
        withVertigram { vertigram, scope ->
            val address = uniqueAddress()

            vertigram.eventBus.consumer<TestPayload, Unit>(scope, address) {
                error("must not execute")
            }.awaitRegistration()

            val failure = expectFailure<VertigramInternalException> {
                vertigram.eventBus.request<String, Unit>(address, "not-a-TestPayload")
            }

            assertEquals("Internal error", failure.message)
        }
    }

    @Test
    fun `Vertigram exception subtype is reconstructed and source stack trace is restored`() = runBlocking {
        withVertigram { vertigram, scope ->
            val address = uniqueAddress()
            val sourceFailure = TestProtocolException("expected")
            val originalStackTrace = sourceFailure.stackTrace.copyOf()

            vertigram.eventBus.consumer<Unit, Unit>(scope, address) {
                throw sourceFailure
            }.awaitRegistration()

            val receivedFailure = expectFailure<TestProtocolException> {
                vertigram.eventBus.request<Unit, Unit>(address, Unit)
            }

            assertEquals("expected", receivedFailure.message)
            assertArrayEquals(originalStackTrace, sourceFailure.stackTrace)
        }
    }

    @Test
    fun `unexpected consumer failure is hidden by default`() = runBlocking {
        withVertigram { vertigram, scope ->
            val address = uniqueAddress()

            vertigram.eventBus.consumer<Unit, Unit>(scope, address) {
                throw IllegalStateException("secret")
            }.awaitRegistration()

            val failure = expectFailure<VertigramInternalException> {
                vertigram.eventBus.request<Unit, Unit>(address, Unit)
            }

            assertEquals("Internal error", failure.message)
        }
    }

    @Test
    fun `unexpected consumer failure message is exposed when configured`() = runBlocking {
        withVertigram(
            Vertigram.Config(hideInternalExceptions = false)
        ) { vertigram, scope ->
            val address = uniqueAddress()

            vertigram.eventBus.consumer<Unit, Unit>(scope, address) {
                throw IllegalStateException("visible")
            }.awaitRegistration()

            val failure = expectFailure<VertigramInternalException> {
                vertigram.eventBus.request<Unit, Unit>(address, Unit)
            }

            assertEquals("visible", failure.message)
        }
    }

    private suspend fun withVertigram(
        config: Vertigram.Config = Vertigram.Config(),
        block: suspend (Vertigram, CoroutineScope) -> Unit
    ) {
        val vertx = Vertx.vertx()
        val scope = vertx.testScope()
        try {
            block(vertx.attachVertigram(config), scope)
        } finally {
            scope.cancel()
            vertx.close().coAwait()
        }
    }

    private fun Vertx.testScope(): CoroutineScope =
        CoroutineScope(
            SupervisorJob() +
                    getOrCreateContext().dispatcher() +
                    CoroutineExceptionHandler { _, _ -> }
        )

    private suspend inline fun <reified T : Throwable> expectFailure(
        crossinline block: suspend () -> Unit
    ): T {
        val failure = try {
            block()
            throw AssertionError("Expected ${T::class.qualifiedName}")
        } catch (t: Throwable) {
            t
        }
        return assertInstanceOf(T::class.java, failure)
    }

    private fun uniqueAddress() = "test.event-bus.${UUID.randomUUID()}"

    private fun testPayload(label: String) =
        TestPayload(UUID.randomUUID(), Instant.parse("2026-07-26T12:34:56Z"), label)

    private data class TestPayload(
        val id: UUID,
        val createdAt: Instant,
        val label: String
    )
}

class TestProtocolException : VertigramException {
    constructor() : super()
    constructor(message: String?) : super(message)
}
