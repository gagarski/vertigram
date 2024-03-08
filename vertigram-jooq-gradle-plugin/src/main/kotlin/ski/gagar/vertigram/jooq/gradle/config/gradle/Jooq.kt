package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.provider.Property
import ski.gagar.vertigram.jooq.app.config.Jooq

abstract class Jooq {
    abstract val driver: Property<String>
    abstract val dbName: Property<String>
    abstract val includes: Property<String>
    abstract val inputSchema: Property<String>
    abstract val packageName: Property<String>
    abstract val directory: Property<String>

    fun pojo(): Jooq = Jooq(
        driver = driver.get(),
        dbName = dbName.get(),
        includes = includes.get(),
        inputSchema = inputSchema.get(),
        packageName = packageName.get(),
        directory = directory.get()
    )

    init {
        directory.convention("build/generated/source/jooq/main")
    }
}