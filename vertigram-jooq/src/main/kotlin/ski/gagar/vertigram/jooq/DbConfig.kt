package ski.gagar.vertigram.jooq

interface DbConfig {
    val jdbcUrl: String
    val username: String
    val password: String
    val flywayLocations: List<String>
        get() = DEFAULT_FLYWAY_LOCATIONS

    companion object {
        val DEFAULT_FLYWAY_LOCATIONS = listOf("classpath:db/migration")
    }
}

data class SimpleDbConfig(override val jdbcUrl: String,
                          override val username: String,
                          override val password: String,
                          override val flywayLocations: List<String> = DbConfig.DEFAULT_FLYWAY_LOCATIONS
) : DbConfig

fun DbConfig(jdbcUrl: String, username: String, password: String) = SimpleDbConfig(jdbcUrl, username, password)
