package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.provider.Property
import ski.gagar.vertigram.jooq.gradle.config.pojo.DatabaseConfig

interface LiveDb {
    val url: Property<String>
    val username: Property<String>
    val password: Property<String>

    fun pojo(): DatabaseConfig.Live {
        return DatabaseConfig.Live(
            url = url.get(),
            username = username.get(),
            password = password.get()
        )
    }
}