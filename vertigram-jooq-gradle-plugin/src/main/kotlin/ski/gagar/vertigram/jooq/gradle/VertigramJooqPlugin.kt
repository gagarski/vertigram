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
import java.util.*

class VertigramJooqPlugin : Plugin<Project> {
    private val version: String
        get() {
            val props = Properties()
            props.load(javaClass.classLoader.getResourceAsStream("vertigram-jooq.properties"))
            return props.getProperty("version")
        }

    override fun apply(project: Project) {
        val extension = project.extensions.create<VertigramJooqExtension>("vertigramJooq")
        val extDepConfig = project.configurations.create("vertigramJooq")

        val appConfig = project.configurations.create("vertigramJooqApp")
        project.dependencies.add(appConfig.name, "ski.gagar.vertigram:vertigram-jooq-app:${Props.version}")

        extDepConfig.setDescription("The classpath used to invoke the Vertigram jOOQ code generator. Add your JDBC driver, generator extensions, and additional dependencies here.")

        val task = project.tasks.register<JavaExec>("vertigramJooqCodegen") {
            val om = ObjectMapper().findAndRegisterModules()
            classpath(appConfig, extDepConfig)
            lateinit var f: File

            mainClass.set("ski.gagar.vertigram.jooq.app.AppKt")
            outputs.dir(project.file(extension.jooq.get().directory.get()))
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
                java.srcDir(task.map { it.outputs });
            }
        }
    }
}
