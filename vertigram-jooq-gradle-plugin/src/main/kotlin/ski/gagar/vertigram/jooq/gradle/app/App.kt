package ski.gagar.vertigram.jooq.gradle.app

import com.fasterxml.jackson.databind.ObjectMapper
import org.testcontainers.containers.JdbcDatabaseContainer
import ski.gagar.vertigram.jooq.gradle.config.gradle.TestContainer
import ski.gagar.vertigram.jooq.gradle.config.pojo.DatabaseConfig
import ski.gagar.vertigram.jooq.gradle.config.pojo.GeneratorConfig
import java.io.File

private fun setUpDb(config: GeneratorConfig): DatabaseConnection {

    return when (config.db) {
        is DatabaseConfig.TestContainer -> setUpTestContainerDb(config.db)
        is DatabaseConfig.Live -> setUpLiveDb(config.db)
    }
}

private fun setUpTestContainerDb(tc: DatabaseConfig.TestContainer): DatabaseConnection {
    val clazz = Class.forName(tc.className)
    val cons = clazz.getConstructor(String::class.java)
    val container = (cons.newInstance("${tc.name}:${tc.version}") as JdbcDatabaseContainer<*>)
        .withUsername(TestContainer.USERNAME)
        .withPassword(TestContainer.PASSWORD)
        .withDatabaseName(TestContainer.DB_NAME)

    container.start()
    return TestContainerConnection(container)
}

private fun setUpLiveDb(live: DatabaseConfig.Live): DatabaseConnection =
    LiveDatabaseConnection(
        url = live.url,
        username = live.username,
        password = live.password
    )


fun main(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("The application expects single argument with config")
    }

    val om = ObjectMapper().findAndRegisterModules()
    val config = om.readValue(File(args[0]), GeneratorConfig::class.java)

    setUpDb(config).use {

    }
}
