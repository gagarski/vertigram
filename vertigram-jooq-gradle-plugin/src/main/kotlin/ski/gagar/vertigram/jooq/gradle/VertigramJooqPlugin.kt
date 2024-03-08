package ski.gagar.vertigram.jooq.gradle

import nu.studer.gradle.jooq.JooqExtension
import org.flywaydb.gradle.FlywayExtension
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.testcontainers.containers.JdbcDatabaseContainer
import ski.gagar.vertigram.jooq.gradle.config.gradle.TestContainer
import ski.gagar.vertigram.jooq.gradle.config.gradle.VertigramJooqExtension
import ski.gagar.vertigram.jooq.gradle.tasks.SetUpDatabase

class VertigramJooqPlugin : Plugin<Project> {
    private fun configurePlugins(project: Project, task: SetUpDatabase) {
        if (null == task.containerInstance)
            return
        val flywayEx = project.extensions.findByType(FlywayExtension::class.java)
            ?: throw GradleException("Flyway plugin expected")

        flywayEx.apply {
            url = task.containerInstance!!.jdbcUrl
        }

        val jooqEx = project.extensions.findByType(JooqExtension::class.java)
            ?: throw GradleException("jOOQ plugin required")

        jooqEx.configurations.all {
            jooqConfiguration.apply {
                jdbc.apply {
                    url = task.containerInstance!!.jdbcUrl
                }

            }
        }
    }

    override fun apply(project: Project) {
        project.plugins.withId("org.flywaydb.flyway") {
            project.plugins.withId("nu.studer.jooq") {
                val extension = project.extensions.create<VertigramJooqExtension>("vertigramJooq")

                val setUpTask = project.tasks.create<SetUpDatabase>("setUpDatabase") {
                    if (!extension.testContainer.isPresent) return@create
                    val tc = extension.testContainer.get()
                    val clazz = Class.forName(tc.className.get())
                    val cons = clazz.getConstructor(String::class.java)
                    val container = (cons.newInstance("${tc.name.get()}:${tc.version.get()}") as JdbcDatabaseContainer<*>)
                        .withUsername(TestContainer.USERNAME)
                        .withPassword(TestContainer.PASSWORD)
                        .withDatabaseName(TestContainer.DB_NAME)

                    container.start()
                    containerInstance = container
                }

                configurePlugins(project, setUpTask)

                project.tasks.named("flywayMigrate").configure {
                    dependsOn("setUpDatabase")
                }

                project.gradle.addBuildListener(object : BuildAdapter() {
                    override fun buildFinished(result: BuildResult) {
                        setUpTask.containerInstance?.stop()
                    }
                })

//                project.tasks.named("generateJooq").configure {
//                    dependsOn("flywayMigrate")
//                }
            }
        }
    }
}
