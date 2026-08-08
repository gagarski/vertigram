package ski.gagar.vertigram.verticles.common

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.attachVertigram
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason
import kotlin.time.Duration.Companion.seconds

class HierarchyVerticleTest {
    @Test
    fun `child dying during start notifies its parent with the original reason`() = runBlocking {
        val vertx = Vertx.vertx()
        val childDeath = CompletableDeferred<DeathNotice>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                ParentVerticle(CompletingChildVerticle(), childDeath)
            )

            val notice = withTimeout(5.seconds) {
                childDeath.await()
            }
            assertEquals(DeathReason.COMPLETED, notice.reason)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `first death request wins and subsequent requests are ignored`() = runBlocking {
        val vertx = Vertx.vertx()
        val childDeath = CompletableDeferred<DeathNotice>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                ParentVerticle(RepeatedDeathChildVerticle(), childDeath)
            )

            val notice = withTimeout(5.seconds) {
                childDeath.await()
            }
            assertEquals(DeathReason.COMPLETED, notice.reason)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `child completing after registration notifies only its parent`() = runBlocking {
        val vertx = Vertx.vertx()
        val childDeath = CompletableDeferred<DeathNotice>()
        val unrelatedChildDeath = CompletableDeferred<DeathNotice>()
        val child = ControllableChildVerticle()
        val parent = ParentVerticle(child, childDeath)

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(UnrelatedHierarchyVerticle(unrelatedChildDeath))
            vertigram.deployVerticle(parent)

            child.completeOnContext()

            val notice = withTimeout(5.seconds) {
                childDeath.await()
            }
            assertEquals(parent.childId, notice.id)
            assertEquals(DeathReason.COMPLETED, notice.reason)

            delay(250)
            assertFalse(unrelatedChildDeath.isCompleted)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `parent undeployment cascades to child and invokes its stop`() = runBlocking {
        val vertx = Vertx.vertx()
        val childStopped = CompletableDeferred<Unit>()
        val child = StopObservingChildVerticle(childStopped)

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(CompletingParentVerticle(child))

            withTimeout(5.seconds) {
                childStopped.await()
            }
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `external child undeployment is reported as failed`() = runBlocking {
        val vertx = Vertx.vertx()
        val childDeath = CompletableDeferred<DeathNotice>()
        val parent = ParentVerticle(PassiveChildVerticle(), childDeath)

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(parent)
            vertigram.undeploy(parent.childId)

            val notice = withTimeout(5.seconds) {
                childDeath.await()
            }
            assertEquals(parent.childId, notice.id)
            assertEquals(DeathReason.FAILED, notice.reason)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `message handler failure fails the verticle`() = runBlocking {
        val vertx = Vertx.vertx()
        val childDeath = CompletableDeferred<DeathNotice>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                ParentVerticle(MessageHandlerFailingChildVerticle(), childDeath)
            )

            val notice = withTimeout(5.seconds) {
                childDeath.await()
            }
            assertEquals(DeathReason.FAILED, notice.reason)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `child death handler failure fails the verticle`() = runBlocking {
        val vertx = Vertx.vertx()
        val childDeath = CompletableDeferred<DeathNotice>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                ParentVerticle(ChildDeathHandlerFailingVerticle(), childDeath)
            )

            val notice = withTimeout(5.seconds) {
                childDeath.await()
            }
            assertEquals(DeathReason.FAILED, notice.reason)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `message handler does not execute after death is requested`() = runBlocking {
        val vertx = Vertx.vertx()
        val childDeath = CompletableDeferred<DeathNotice>()
        val handled = CompletableDeferred<Unit>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(
                ParentVerticle(MessageHandlerAfterDeathChildVerticle(handled), childDeath)
            )

            withTimeout(5.seconds) {
                childDeath.await()
            }
            assertFalse(handled.isCompleted)
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `configured child receives typed config alongside hierarchy metadata`() = runBlocking {
        val vertx = Vertx.vertx()
        val receivedConfig = CompletableDeferred<String>()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(ConfiguredParentVerticle(receivedConfig))

            assertEquals(
                "configured",
                withTimeout(5.seconds) {
                    receivedConfig.await()
                }
            )
        } finally {
            vertx.close().coAwait()
        }
    }

    private class ParentVerticle(
        private val child: VertigramVerticle<Unit?>,
        private val childDeath: CompletableDeferred<DeathNotice>
    ) : HierarchyVerticle<Unit?>() {
        lateinit var childId: String
            private set

        override suspend fun start() {
            super.start()
            childId = deployChild(child)
        }

        override suspend fun onChildDeath(deathNotice: DeathNotice) {
            childDeath.complete(deathNotice)
        }
    }

    private class CompletingChildVerticle : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            complete()
        }
    }

    private class RepeatedDeathChildVerticle : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            complete()
            fail()
            cancel()
        }
    }

    private class ControllableChildVerticle : HierarchyVerticle<Unit?>() {
        fun completeOnContext() {
            vertx.runOnContext {
                complete()
            }
        }
    }

    private class PassiveChildVerticle : HierarchyVerticle<Unit?>()

    private class UnrelatedHierarchyVerticle(
        private val childDeath: CompletableDeferred<DeathNotice>
    ) : HierarchyVerticle<Unit?>() {
        override suspend fun onChildDeath(deathNotice: DeathNotice) {
            childDeath.complete(deathNotice)
        }
    }

    private class CompletingParentVerticle(
        private val child: VertigramVerticle<Unit?>
    ) : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            deployChild(child)
            complete()
        }
    }

    private class StopObservingChildVerticle(
        private val stopped: CompletableDeferred<Unit>
    ) : HierarchyVerticle<Unit?>() {
        override suspend fun stop() {
            stopped.complete(Unit)
            super.stop()
        }
    }

    private class MessageHandlerFailingChildVerticle : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            messageHandler {
                throw IllegalStateException("Expected message-handler failure")
            }
        }
    }

    private class ChildDeathHandlerFailingVerticle : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            deployChild(CompletingChildVerticle())
        }

        override suspend fun onChildDeath(deathNotice: DeathNotice) {
            throw IllegalStateException("Expected child-death handler failure")
        }
    }

    private class MessageHandlerAfterDeathChildVerticle(
        private val handled: CompletableDeferred<Unit>
    ) : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            complete()
            messageHandler {
                handled.complete(Unit)
            }
        }
    }

    private class ConfiguredParentVerticle(
        private val receivedConfig: CompletableDeferred<String>
    ) : HierarchyVerticle<Unit?>() {
        override suspend fun start() {
            super.start()
            deployChild(
                ConfiguredChildVerticle(receivedConfig),
                ChildConfig("configured")
            )
        }
    }

    private class ConfiguredChildVerticle(
        private val receivedConfig: CompletableDeferred<String>
    ) : HierarchyVerticle<ChildConfig>() {
        override suspend fun start() {
            super.start()
            receivedConfig.complete(typedConfig.value)
        }
    }

    private data class ChildConfig(val value: String)
}
