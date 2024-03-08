package ski.gagar.vertigram.jooq.app.config

data class Jooq(
    val driver: String,
    val dbName: String,
    val includes: String,
    val inputSchema: String,
    val packageName: String,
    val directory: String
)