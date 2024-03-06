package ski.gagar.vertigram.jooq.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create

interface VertigramJooqPluginExtension {
    val hello: Property<String>
    val somebody: Property<String>
}

class VertigramJooqPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<VertigramJooqPluginExtension>("vertigramJooq")
        project.task("hello") {
            doLast {
                println("${extension.hello.get()} from ${extension.somebody.get()}")
            }
        }
    }
}