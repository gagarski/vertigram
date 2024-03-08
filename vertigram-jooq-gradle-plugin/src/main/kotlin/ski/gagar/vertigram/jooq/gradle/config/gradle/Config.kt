package ski.gagar.vertigram.jooq.gradle.config.gradle

import nu.studer.gradle.jooq.JooqExtension
import org.flywaydb.gradle.FlywayExtension
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Optional

abstract class Config(val objectFactory: ObjectFactory) {
    abstract val generator: Property<String>
    abstract val jooqDatabase: Property<String>
    abstract val schema: Property<String>
    abstract val driver: Property<String>
    abstract val packageName: Property<String>
    abstract val directory: Property<String>
    @get:Optional
    abstract val testContainer: Property<TestContainer>
    @get:Optional
    abstract val liveDb: Property<LiveDb>

    fun testContainer(action: Action<TestContainer>) {
        val tc = objectFactory.newInstance(TestContainer::class.java)
        action.execute(tc)
        testContainer.set(tc)
    }

    fun liveDb(action: Action<LiveDb>) {
        val live = objectFactory.newInstance(LiveDb::class.java)
        action.execute(live)
        liveDb.set(live)
    }

    internal fun propagateTo(project: Project) {
        propagateCommon(project)

        if (testContainer.isPresent && liveDb.isPresent) {
            throw GradleException("testContainer and liveDb are mutually exclusive")
        }

        if (liveDb.isPresent) {
            propagateLiveDb(project, liveDb.get())
        }

        if (testContainer.isPresent) {
            propagateTestContainer(project)
        }
    }

    private fun propagateLiveDb(project: Project, liveDb: LiveDb) {
        val flywayEx = project.extensions.findByType(FlywayExtension::class.java)
            ?: throw GradleException("Flyway plugin expected")

        flywayEx.apply {
            liveDb.url.map {
                url = it
            }
            liveDb.username.map {
                user = it
            }
            liveDb.password.map {
                password = it
            }
        }

        val jooqEx = project.extensions.findByType(JooqExtension::class.java)
            ?: throw GradleException("jOOQ plugin required")

        jooqEx.configurations.all {
            jooqConfiguration.apply {
                jdbc.apply {
                    liveDb.url.map {
                        url = it
                    }
                    liveDb.username.map {
                        username = it
                    }
                    liveDb.password.map {
                        password = it
                    }
                }

            }
        }
    }

    private fun propagateTestContainer(project: Project) {
        val flywayEx = project.extensions.findByType(FlywayExtension::class.java)
            ?: throw GradleException("Flyway plugin expected")

        flywayEx.apply {
            user = TestContainer.USERNAME
            password = TestContainer.PASSWORD
        }

        val jooqEx = project.extensions.findByType(JooqExtension::class.java)
            ?: throw GradleException("jOOQ plugin required")

        jooqEx.configurations.all {
            jooqConfiguration.apply {
                jdbc.apply {
                    username = TestContainer.USERNAME
                    password = TestContainer.PASSWORD
                }

            }
        }
    }

    private fun propagateCommon(project: Project) {
        val flywayEx = project.extensions.findByType(FlywayExtension::class.java)
            ?: throw GradleException("Flyway plugin expected")

        flywayEx.apply {
            this@Config.driver.map {
                driver = it
            }
        }

        val jooqEx = project.extensions.findByType(JooqExtension::class.java)
            ?: throw GradleException("jOOQ plugin required")

        jooqEx.configurations.create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    this@Config.driver.map {
                        driver = it
                    }
                }
                generator.apply {
                    this@Config.generator.map {
                        name = it
                    }
                    database.apply {
                        jooqDatabase.map {
                            name = it
                        }

                    }
                    target.apply {
                        this@Config.packageName.map {
                            packageName = it
                        }
                        this@Config.directory.map {
                            directory = it
                        }

                    }
                }

            }
        }
    }
}