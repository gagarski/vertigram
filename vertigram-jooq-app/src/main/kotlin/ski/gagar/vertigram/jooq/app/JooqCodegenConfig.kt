package ski.gagar.vertigram.jooq.app

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonRootName
import ski.gagar.vertigram.jooq.app.config.GeneratorConfig

@JsonRootName("configuration")
@JsonInclude(Include.NON_NULL)
data class JooqCodegenConfig(
    val jdbc: Jdbc,
    val generator: Generator
) {
    data class Jdbc(
        val driver: String,
        val url: String,
        val user: String,
        val password: String
    )
    data class Generator(
        val name: String? = null,
        val database: Database,
        val generate: Generate,
        val target: Target
    ) {
        data class Database(
            val name: String,
            val includes: String,
            val excludes: String? = null,
            val inputSchema: String
        )

        data object Generate

        data class Target(
            val packageName: String,
            val directory: String
        )
    }

    companion object {
        fun from(config: GeneratorConfig, dbConn: DatabaseConnection) =
            JooqCodegenConfig(
                jdbc = Jdbc(
                    driver = config.jooq.driver,
                    url = dbConn.url,
                    user = dbConn.username,
                    password = dbConn.password
                ),
                generator = Generator(
                    database = Generator.Database(
                        name = config.jooq.dbName,
                        includes = config.jooq.includes,
                        excludes = null,
                        inputSchema = config.jooq.inputSchema
                    ),
                    generate = Generator.Generate,
                    target = Generator.Target(
                        packageName = config.jooq.packageName,
                        directory = config.jooq.directory
                    )
                )
            )
    }
}