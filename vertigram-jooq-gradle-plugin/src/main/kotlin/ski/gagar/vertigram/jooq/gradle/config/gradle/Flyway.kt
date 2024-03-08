package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.provider.ListProperty
import ski.gagar.vertigram.jooq.app.config.Flyway

abstract class Flyway {
    abstract val locations: ListProperty<String>

    init {
        locations.convention(listOf("filesystem:src/main/resources/db/migration"))
    }

    fun pojo() = Flyway(locations.get())
}