package ski.gagar.vertigram.jooq.app

import org.testcontainers.containers.JdbcDatabaseContainer
import ski.gagar.vertigram.jooq.app.config.DatabaseConfig

sealed interface DatabaseConnection : AutoCloseable {
    val url: String
    val username: String
    val password: String

    fun tearDown()

    override fun close() {
        tearDown()
    }
}

data class LiveDatabaseConnection(
    override val url: String,
    override val username: String,
    override val password: String
) : DatabaseConnection {
    override fun tearDown() {}
}

data class TestContainerConnection(
    val container: JdbcDatabaseContainer<*>
) : DatabaseConnection {
    override val url: String = container.jdbcUrl
    override val username: String = DatabaseConfig.TestContainer.USERNAME
    override val password: String = DatabaseConfig.TestContainer.PASSWORD

    override fun tearDown() {
        container.stop()
    }
}
