package ski.gagar.vertigram.jooq.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.testcontainers.containers.JdbcDatabaseContainer

abstract class SetUpDatabase : DefaultTask() {
    @Internal
    var containerInstance: JdbcDatabaseContainer<*>? = null

    @TaskAction
    fun execute() {

    }
}