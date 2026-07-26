package ski.gagar.vertigram.verticles.common

import com.fasterxml.jackson.core.type.TypeReference
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.attachVertigram
import ski.gagar.vertigram.awaitRegistration
import ski.gagar.vertigram.util.jackson.typeReference
import java.time.Duration
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class PostOfficeVerticleTest {
    @Test
    fun `message received before subscription is replayed`() = runBlocking {
        withPostOffice { vertigram, postOffice ->
            val subscriber = deploySubscriber(vertigram)
            val message = TestMessage(TestDiscriminator("one"), "stored")

            vertigram.deliver(postOffice.incomingAddress, message)
            vertigram.deliver(
                postOffice.subscribeAddress,
                TestSubscriptionInfo(subscriber.address, message.discriminator)
            )

            assertEquals(message, subscriber.nextMessage())
        }
    }

    @Test
    fun `live message is forwarded to all matching subscribers only`() = runBlocking {
        withPostOffice { vertigram, postOffice ->
            val first = deploySubscriber(vertigram)
            val second = deploySubscriber(vertigram)
            val other = deploySubscriber(vertigram)
            val discriminator = TestDiscriminator("matching")

            vertigram.deliver(
                postOffice.subscribeAddress,
                TestSubscriptionInfo(first.address, discriminator)
            )
            vertigram.deliver(
                postOffice.subscribeAddress,
                TestSubscriptionInfo(second.address, discriminator)
            )
            vertigram.deliver(
                postOffice.subscribeAddress,
                TestSubscriptionInfo(other.address, TestDiscriminator("other"))
            )

            val message = TestMessage(discriminator, "live")
            vertigram.deliver(postOffice.incomingAddress, message)

            assertEquals(message, first.nextMessage())
            assertEquals(message, second.nextMessage())
            other.assertNoMessage()
        }
    }

    @Test
    fun `resubscription replays only messages missed while unsubscribed`() = runBlocking {
        withPostOffice { vertigram, postOffice ->
            val subscriber = deploySubscriber(vertigram)
            val discriminator = TestDiscriminator("one")
            val subscription = TestSubscriptionInfo(subscriber.address, discriminator)

            vertigram.deliver(postOffice.subscribeAddress, subscription)

            val delivered = TestMessage(discriminator, "delivered")
            vertigram.deliver(postOffice.incomingAddress, delivered)
            assertEquals(delivered, subscriber.nextMessage())

            vertigram.deliver(postOffice.unsubscribeAddress, subscription)

            val missed = TestMessage(discriminator, "missed")
            vertigram.deliver(postOffice.incomingAddress, missed)
            subscriber.assertNoMessage()

            vertigram.deliver(postOffice.subscribeAddress, subscription)
            assertEquals(missed, subscriber.nextMessage())

            val live = TestMessage(discriminator, "live")
            vertigram.deliver(postOffice.incomingAddress, live)
            assertEquals(live, subscriber.nextMessage())
            subscriber.assertNoMessage()
        }
    }

    @Test
    fun `subscription identity is its address within a discriminator`() = runBlocking {
        withPostOffice { vertigram, postOffice ->
            val subscriber = deploySubscriber(vertigram)
            val discriminator = TestDiscriminator("one")

            vertigram.deliver(
                postOffice.subscribeAddress,
                TestSubscriptionInfo(subscriber.address, discriminator, variant = "first")
            )
            vertigram.deliver(
                postOffice.subscribeAddress,
                TestSubscriptionInfo(subscriber.address, discriminator, variant = "replacement")
            )
            vertigram.deliver(
                postOffice.unsubscribeAddress,
                TestSubscriptionInfo(subscriber.address, discriminator, variant = "different-instance")
            )

            vertigram.deliver(
                postOffice.incomingAddress,
                TestMessage(discriminator, "must-not-arrive")
            )
            subscriber.assertNoMessage()
        }
    }

    @Test
    fun `acceptance subscription and per-message policies are applied`() = runBlocking {
        val postOffice = TestPostOfficeVerticle(
            shouldAccept = { it.body != "rejected" },
            shouldAllow = { it.variant != "denied" },
            shouldPass = { message, _ -> message.body != "blocked" }
        )

        withPostOffice(postOffice) { vertigram, deployedPostOffice ->
            val allowed = deploySubscriber(vertigram)
            val denied = deploySubscriber(vertigram)
            val discriminator = TestDiscriminator("one")

            vertigram.deliver(
                deployedPostOffice.incomingAddress,
                TestMessage(discriminator, "rejected")
            )
            vertigram.deliver(
                deployedPostOffice.subscribeAddress,
                TestSubscriptionInfo(allowed.address, discriminator)
            )
            allowed.assertNoMessage()

            vertigram.deliver(
                deployedPostOffice.subscribeAddress,
                TestSubscriptionInfo(denied.address, discriminator, variant = "denied")
            )
            vertigram.deliver(
                deployedPostOffice.incomingAddress,
                TestMessage(discriminator, "blocked")
            )
            allowed.assertNoMessage()
            denied.assertNoMessage()

            val accepted = TestMessage(discriminator, "accepted")
            vertigram.deliver(deployedPostOffice.incomingAddress, accepted)
            assertEquals(accepted, allowed.nextMessage())
            denied.assertNoMessage()
        }
    }

    @Test
    fun `expired stored message is not replayed`() = runBlocking {
        val postOffice = TestPostOfficeVerticle(
            storagePeriod = Duration.ofMillis(80),
            cleanupPeriod = Duration.ofMillis(20)
        )

        withPostOffice(postOffice) { vertigram, deployedPostOffice ->
            val subscriber = deploySubscriber(vertigram)
            val discriminator = TestDiscriminator("one")

            vertigram.deliver(
                deployedPostOffice.incomingAddress,
                TestMessage(discriminator, "expired")
            )

            kotlinx.coroutines.delay(250)

            vertigram.deliver(
                deployedPostOffice.subscribeAddress,
                TestSubscriptionInfo(subscriber.address, discriminator)
            )
            subscriber.assertNoMessage()
        }
    }

    private suspend fun withPostOffice(
        postOffice: TestPostOfficeVerticle = TestPostOfficeVerticle(),
        block: suspend (Vertigram, TestPostOfficeVerticle) -> Unit
    ) {
        val vertx = Vertx.vertx()
        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(postOffice)
            block(vertigram, postOffice)
        } finally {
            vertx.close().coAwait()
        }
    }

    private suspend fun deploySubscriber(vertigram: Vertigram): RecordingVerticle {
        val subscriber = RecordingVerticle()
        vertigram.deployVerticle(subscriber)
        return subscriber
    }

    private suspend fun <Request> Vertigram.deliver(
        address: String,
        request: Request
    ) {
        eventBus.request<Request, Unit>(address, request)
    }

    private suspend fun RecordingVerticle.nextMessage(): TestMessage =
        withTimeout(5.seconds) {
            messages.receive()
        }

    private suspend fun RecordingVerticle.assertNoMessage() {
        assertNull(
            withTimeoutOrNull(200.milliseconds) {
                messages.receive()
            }
        )
    }

    private data class TestDiscriminator(
        val value: String
    ) : PostOfficeVerticle.Discriminator

    private data class TestMessage(
        val discriminator: TestDiscriminator,
        val body: String
    )

    private data class TestSubscriptionInfo(
        override val address: String,
        override val discriminator: TestDiscriminator,
        val variant: String = ""
    ) : PostOfficeVerticle.SubscriptionInfo<TestDiscriminator>

    private class TestPostOfficeVerticle(
        override val storagePeriod: Duration = Duration.ofSeconds(5),
        override val cleanupPeriod: Duration = storagePeriod.dividedBy(2),
        private val shouldAccept: (TestMessage) -> Boolean = { true },
        private val shouldAllow: (TestSubscriptionInfo) -> Boolean = { true },
        private val shouldPass: (TestMessage, TestSubscriptionInfo) -> Boolean = { _, _ -> true }
    ) : PostOfficeVerticle<Unit?, TestMessage, TestDiscriminator, TestSubscriptionInfo>() {
        private val addressPrefix = "test.post-office.${UUID.randomUUID()}"

        override val incomingAddress = "$addressPrefix.incoming"
        override val subscribeAddress = "$addressPrefix.subscribe"
        override val unsubscribeAddress = "$addressPrefix.unsubscribe"
        override val messageTypeRef: TypeReference<TestMessage> = typeReference()
        override val subInfoTypeRef: TypeReference<TestSubscriptionInfo> = typeReference()

        override fun discriminate(msg: TestMessage): TestDiscriminator = msg.discriminator

        override suspend fun shouldAcceptMessage(msg: TestMessage): Boolean =
            shouldAccept(msg)

        override suspend fun shouldAllowSubscribe(subRequest: TestSubscriptionInfo): Boolean =
            shouldAllow(subRequest)

        override suspend fun shouldPassMessageToSubscriber(
            msg: TestMessage,
            subInfo: TestSubscriptionInfo
        ): Boolean = shouldPass(msg, subInfo)
    }

    private class RecordingVerticle : VertigramVerticle<Unit?>() {
        val address = "test.post-office.subscriber.${UUID.randomUUID()}"
        val messages = Channel<TestMessage>(Channel.UNLIMITED)

        override suspend fun start() {
            super.start()
            consumer<TestMessage, Unit>(address) {
                messages.send(it)
            }.awaitRegistration()
        }
    }
}
