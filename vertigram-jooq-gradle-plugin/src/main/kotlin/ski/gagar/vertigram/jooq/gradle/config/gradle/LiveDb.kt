package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.provider.Property

interface LiveDb {
    val url: Property<String>
    val username: Property<String>
    val password: Property<String>
}