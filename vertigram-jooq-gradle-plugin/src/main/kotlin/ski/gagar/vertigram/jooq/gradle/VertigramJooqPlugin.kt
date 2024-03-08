package ski.gagar.vertigram.jooq.gradle

import com.fasterxml.jackson.databind.ObjectMapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import ski.gagar.vertigram.jooq.gradle.config.gradle.VertigramJooqExtension
import java.io.File
import java.net.URLClassLoader

class VertigramJooqPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<VertigramJooqExtension>("vertigramJooq")
        val config = project.configurations.create("vertigramJooq")
        config.setDescription("The classpath used to invoke the Vertigram jOOQ code generator. Add your JDBC driver, generator extensions, and additional dependencies here.")

        project.tasks.register<JavaExec>("vertigramJooqCodegen") {
            val om = ObjectMapper().findAndRegisterModules()
            val pluginCp = project.files((VertigramJooqPlugin::class.java.classLoader as URLClassLoader).urLs.map { it.path })
            classpath(pluginCp, config)
            lateinit var f: File

            mainClass.set("ski.gagar.vertigram.jooq.gradle.app.AppKt")

            doFirst {
                f = File.createTempFile("vertigram-jooq-", ".json")
                om.writeValue(f, extension.pojo())
                args(f.path)
            }


            doLast {
                f.delete()
            }
        }
    }
}
