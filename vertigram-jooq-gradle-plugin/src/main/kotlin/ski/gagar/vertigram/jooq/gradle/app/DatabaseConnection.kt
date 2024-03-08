package ski.gagar.vertigram.jooq.gradle.app

import org.testcontainers.containers.JdbcDatabaseContainer

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
    override val username: String = container.username
    override val password: String = container.password

    override fun tearDown() {
        container.stop()
    }
}
