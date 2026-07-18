package ski.gagar.vertigram.services.logback

import io.vertx.core.Vertx
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.attachVertigram

class OptionalLogbackInitializationTest {
    @Test
    fun `Vertigram starts without Logback`() {
        val vertx = Vertx.vertx()

        try {
            assertDoesNotThrow {
                vertx.attachVertigram()
            }
        } finally {
            vertx.close().toCompletionStage().toCompletableFuture().join()
        }
    }
}
