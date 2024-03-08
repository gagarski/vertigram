package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.provider.Property
import ski.gagar.vertigram.jooq.app.config.DatabaseConfig

interface TestContainer {
    val className: Property<String>
    val name: Property<String>
    val version: Property<String>

    fun pojo(): DatabaseConfig.TestContainer =
        DatabaseConfig.TestContainer(
            className = className.get(),
            name = name.get(),
            version = version.get()
        )
}