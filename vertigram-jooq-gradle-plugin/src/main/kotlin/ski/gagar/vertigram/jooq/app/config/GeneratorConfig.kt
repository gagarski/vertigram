package ski.gagar.vertigram.jooq.app.config

data class GeneratorConfig(
    val db: DatabaseConfig,
    val flyway: Flyway,
    val jooq: Jooq
)
