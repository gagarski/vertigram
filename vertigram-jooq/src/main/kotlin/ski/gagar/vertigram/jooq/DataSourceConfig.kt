package ski.gagar.vertigram.jooq

interface DataSourceConfig {
    val jdbcUrl: String
    val username: String
    val password: String
    val flywayLocations: List<String>
        get() = DEFAULT_FLYWAY_LOCATIONS

    companion object {
        val DEFAULT_FLYWAY_LOCATIONS = listOf("classpath:db/migration")
    }
}

data class SimpleDataSourceConfig(override val jdbcUrl: String,
                                  override val username: String,
                                  override val password: String,
                                  override val flywayLocations: List<String> = DataSourceConfig.DEFAULT_FLYWAY_LOCATIONS
) : DataSourceConfig

fun DataSourceConfig(jdbcUrl: String, username: String, password: String) = SimpleDataSourceConfig(jdbcUrl, username, password)
