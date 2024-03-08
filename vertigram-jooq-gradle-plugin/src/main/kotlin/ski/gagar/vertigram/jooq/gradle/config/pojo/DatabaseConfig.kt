package ski.gagar.vertigram.jooq.gradle.config.pojo

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(DatabaseConfig.Live::class),
    JsonSubTypes.Type(DatabaseConfig.TestContainer::class)
)
sealed interface DatabaseConfig {
    data class Live(
        val url: String,
        val username: String,
        val password: String
    ) : DatabaseConfig

    data class TestContainer(
        val className: String,
        val name: String,
        val version: String
    ) : DatabaseConfig
}