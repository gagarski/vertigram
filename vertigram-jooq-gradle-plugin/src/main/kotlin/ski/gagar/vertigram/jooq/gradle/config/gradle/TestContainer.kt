package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.provider.Property

interface TestContainer {
    val className: Property<String>
    val name: Property<String>
    val version: Property<String>

    companion object {
        const val USERNAME = "vertigram_jooq"
        const val PASSWORD = "secret"
        const val DB_NAME = "vertigram_jooq"
    }
}