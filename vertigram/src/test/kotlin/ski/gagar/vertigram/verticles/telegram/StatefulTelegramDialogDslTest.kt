package ski.gagar.vertigram.verticles.telegram

import java.time.Instant
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.attachVertigram
import ski.gagar.vertigram.telegram.types.Chat
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.create

class StatefulTelegramDialogDslTest {
    @Test
    fun `dsl verticle preserves config type during deployment`() = runBlocking {
        val received = CompletableDeferred<TestConfig>()
        val definition = dialog<TestConfig> {
            val initial by state()

            chatId { it.chatId }
            initial { initial() }
            initial.define {
                sideEffect {
                    received.complete(config)
                    complete()
                }
            }
        }
        val vertx = Vertx.vertx()

        try {
            val vertigram = vertx.attachVertigram()
            vertigram.deployVerticle(definition.createVerticle(), TestConfig(42))

            assertEquals(TestConfig(42), withTimeout(5_000) { received.await() })
        } finally {
            vertx.close().coAwait()
        }
    }

    @Test
    fun `state factories support zero through five arguments and delegated names`() {
        val captured = mutableListOf<List<Any?>>()
        lateinit var targets: List<DialogStateTarget<TestConfig>>

        val definition = dialog<TestConfig> {
            val zero by state()
            val one by state<Int>()
            val two by state<Int, String>()
            val three by state<Int, String, Boolean>()
            val four by state<Int, String, Boolean, Long>()
            val five by state<Int, String, Boolean, Long, Double>()

            chatId { it.chatId }
            initial { zero() }

            zero.define { captured.add(emptyList()) }
            one.define { a -> captured.add(listOf(a)) }
            two.define { a, b -> captured.add(listOf(a, b)) }
            three.define { a, b, c -> captured.add(listOf(a, b, c)) }
            four.define { a, b, c, d -> captured.add(listOf(a, b, c, d)) }
            five.define { a, b, c, d, e -> captured.add(listOf(a, b, c, d, e)) }

            targets = listOf(
                zero(),
                one(1),
                two(1, "two"),
                three(1, "three", true),
                four(1, "four", true, 4L),
                five(1, "five", true, 5L, 5.5)
            )
        }

        targets.forEach { StateBuilder<TestConfig>().apply(it.configure) }

        assertEquals(42, definition.chatIdProvider(TestConfig(42)))
        assertEquals(listOf("zero", "one", "two", "three", "four", "five"),
            targets.map { it.definition.displayName })
        assertEquals(
            listOf(
                emptyList(),
                listOf(1),
                listOf(1, "two"),
                listOf(1, "three", true),
                listOf(1, "four", true, 4L),
                listOf(1, "five", true, 5L, 5.5)
            ),
            captured
        )
    }

    @Test
    fun `declared handles support forward references and cycles`() {
        dialog<TestConfig> {
            val first by state()
            val second by state<Int>()

            chatId { it.chatId }
            initial { first() }
            first.define {
                onMessage {
                    become(second(1))
                    true
                }
            }
            second.define {
                onMessage {
                    become(first())
                    true
                }
            }
        }
    }

    @Test
    fun `dialog rejects incomplete definitions`() {
        assertThrows(IllegalStateException::class.java) {
            dialog<TestConfig> {
                state()
                chatId { it.chatId }
                initial { error("not evaluated while building") }
            }
        }
    }

    @Test
    fun `filters side effects and message handlers are ordered and short circuit`() = runBlocking {
        val calls = mutableListOf<String>()
        lateinit var initial: DialogStateTarget<TestConfig>
        lateinit var firstTarget: DialogStateTarget<TestConfig>
        lateinit var latestTarget: DialogStateTarget<TestConfig>

        dialog<TestConfig> {
            val source by state()
            val first by state()
            val latest by state()

            chatId { it.chatId }
            initial { source() }
            first.define {}
            latest.define {}
            source.define {
                filterMessage { calls += "filter-1"; true }
                filterMessage { calls += "filter-2"; true }
                sideEffect { calls += "effect-1" }
                sideEffect { calls += "effect-2" }
                onMessage {
                    calls += "handler-1"
                    become(first())
                    false
                }
                onMessage {
                    calls += "handler-2"
                    become(latest())
                    true
                }
                onMessage {
                    calls += "handler-3"
                    true
                }
            }
            initial = source()
            firstTarget = first()
            latestTarget = latest()
        }

        val behavior = StateBuilder<TestConfig>().apply(initial.configure)
        val verticle = BareDialog()
        val config = TestConfig(1)
        val message = message()
        val filterScope = MessageFilterScope(config, verticle)
        assertTrue(behavior.filters.all { it(filterScope, message) })

        val actionScope = StateActionScope(config, verticle, { _, _, _ -> }, {})
        behavior.sideEffects.forEach { it(actionScope) }
        for (handler in behavior.handlers) if (handler(actionScope, message)) break

        assertEquals(
            listOf("filter-1", "filter-2", "effect-1", "effect-2", "handler-1", "handler-2"),
            calls
        )
        assertSame(latestTarget.definition, actionScope.pendingTransition?.target?.definition)
        assertTrue(actionScope.pendingTransition?.target?.definition !== firstTarget.definition)
        assertSame(verticle, actionScope.verticle)
        assertSame(config, actionScope.config)
    }

    private fun message() = Message.create(
        messageId = 1,
        date = Instant.EPOCH,
        chat = Chat.create(id = 1, type = Chat.Type.PRIVATE)
    )

    private data class TestConfig(val chatId: Long)

    private class BareDialog : StatefulTelegramDialogVerticle<TestConfig>() {
        override val chatId: Long = 1
        override val initialState: State = object : State(this) {}
    }
}
