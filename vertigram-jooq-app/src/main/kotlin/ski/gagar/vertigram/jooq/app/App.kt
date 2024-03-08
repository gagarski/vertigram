package ski.gagar.vertigram.jooq.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jooq.codegen.GenerationTool
import org.testcontainers.containers.JdbcDatabaseContainer
import ski.gagar.vertigram.jooq.app.config.DatabaseConfig
import ski.gagar.vertigram.jooq.app.config.GeneratorConfig
import ski.gagar.vertigram.jooq.app.util.withTmpFile
import java.io.File
import javax.sql.DataSource

private fun setUpDb(config: GeneratorConfig): DatabaseConnection {
    return when (val db = config.db) {
        is DatabaseConfig.TestContainer -> setUpTestContainerDb(db)
        is DatabaseConfig.Live -> setUpLiveDb(db)
    }
}

private fun setUpTestContainerDb(tc: DatabaseConfig.TestContainer): DatabaseConnection {
    val clazz = Class.forName(tc.className)
    val cons = clazz.getConstructor(String::class.java)
    val container = (cons.newInstance("${tc.name}:${tc.version}") as JdbcDatabaseContainer<*>)
        .withUsername(DatabaseConfig.TestContainer.USERNAME)
        .withPassword(DatabaseConfig.TestContainer.PASSWORD)
        .withDatabaseName(DatabaseConfig.TestContainer.DB_NAME)

    container.start()
    return TestContainerConnection(container)
}

private fun setUpLiveDb(live: DatabaseConfig.Live): DatabaseConnection =
    LiveDatabaseConnection(
        url = live.url,
        username = live.username,
        password = live.password
    )

fun DatabaseConnection.connect(): HikariDataSource =
    HikariDataSource().apply dsConstruct@ {
        this.jdbcUrl = this@connect.url
        this@connect.username.let {
            this@dsConstruct.username = this@connect.username
        }
        this@connect.password.let {
            this@dsConstruct.password = this@connect.password
        }

    }

fun DataSource.runFlyway(config: GeneratorConfig) {
    println(config.flyway.locations)
    Flyway.configure()
        .dataSource(this)
        .locations(*config.flyway.locations.toTypedArray())
        .load()
        .migrate()
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("The application expects single argument with config")
    }

    val om = ObjectMapper().findAndRegisterModules()
    val config = om.readValue(File(args[0]), GeneratorConfig::class.java)
    setUpDb(config).use { c ->
        c.connect().use {
            it.runFlyway(config)
        }

        withTmpFile { f ->
            val xmlMapper = XmlMapper()
            xmlMapper.writeValue(f, JooqCodegenConfig.from(config, c))
            GenerationTool.main(arrayOf(f.path))
        }

    }
}
