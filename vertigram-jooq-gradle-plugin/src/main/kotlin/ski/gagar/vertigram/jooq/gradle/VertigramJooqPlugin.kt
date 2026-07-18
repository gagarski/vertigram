package ski.gagar.vertigram.jooq.gradle

import com.fasterxml.jackson.databind.ObjectMapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import ski.gagar.vertigram.jooq.gradle.config.gradle.Props
import ski.gagar.vertigram.jooq.gradle.config.gradle.VertigramJooqExtension
import java.io.File

class VertigramJooqPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<VertigramJooqExtension>("vertigramJooq")
        val extDepConfig = project.configurations.create("vertigramJooq")

        val runnerConfig = project.configurations.create("vertigramJooqRunner") {
            description = "The isolated runtime classpath for the Vertigram jOOQ runner."
            isCanBeConsumed = false
            isCanBeResolved = true
            isVisible = false
        }
        Props.runnerDependencies.forEach {
            project.dependencies.add(runnerConfig.name, it)
        }

        extDepConfig.setDescription("The classpath used to invoke the Vertigram jOOQ code generator. Add your JDBC driver, generator extensions, and additional dependencies here.")

        val task = project.tasks.register<JavaExec>("vertigramJooqCodegen") {
            val om = ObjectMapper()
            val pluginClasses = File(VertigramJooqPlugin::class.java.protectionDomain.codeSource.location.toURI())
            classpath(project.files(pluginClasses), runnerConfig, extDepConfig)
            lateinit var f: File

            mainClass.set("ski.gagar.vertigram.jooq.app.AppKt")
            outputs.dir(project.provider { project.file(extension.jooq.get().directory.get()) })
            doFirst {
                f = File.createTempFile("vertigram-jooq-", ".json")
                om.writeValue(f, extension.pojo())
                args(f.path)
            }


            doLast {
                f.delete()
            }
        }

        project.extensions.getByType(SourceSetContainer::class.java).configureEach {
            if (name == "main") {
                java.srcDir(project.provider { project.file(extension.jooq.get().directory.get()) })
            }
        }

        project.tasks.matching {
            it.name in setOf("compileJava", "compileKotlin")
        }.configureEach {
            dependsOn(task)
        }
    }
}
