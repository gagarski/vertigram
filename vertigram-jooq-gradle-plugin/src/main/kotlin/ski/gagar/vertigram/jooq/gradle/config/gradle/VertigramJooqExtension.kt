package ski.gagar.vertigram.jooq.gradle.config.gradle

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Optional
import ski.gagar.vertigram.jooq.app.config.GeneratorConfig
import javax.inject.Inject


abstract class VertigramJooqExtension @Inject constructor(val objectFactory: ObjectFactory, val project: Project) {
    @get:Optional
    abstract val testContainer: Property<TestContainer>
    @get:Optional
    abstract val liveDb: Property<LiveDb>
    abstract val flyway: Property<Flyway>
    abstract val jooq: Property<Jooq>

    init {
        flyway.convention(objectFactory.newInstance(Flyway::class.java))
        jooq.convention(objectFactory.newInstance(Jooq::class.java))
    }

    fun testContainer(action: Action<TestContainer>) {
        val tc = if (testContainer.isPresent) testContainer.get() else objectFactory.newInstance(TestContainer::class.java)
        action.execute(tc)
        testContainer.set(tc)
    }

    fun liveDb(action: Action<LiveDb>) {
        val live = if (liveDb.isPresent) liveDb.get() else objectFactory.newInstance(LiveDb::class.java)
        action.execute(live)
        liveDb.set(live)
    }

    fun flyway(action: Action<Flyway>) {
        val fw = if (flyway.isPresent) flyway.get() else objectFactory.newInstance(Flyway::class.java)
        action.execute(fw)
        flyway.set(fw)
    }

    fun jooq(action: Action<Jooq>) {
        val jooq = if (jooq.isPresent) jooq.get() else objectFactory.newInstance(Jooq::class.java)
        action.execute(jooq)
        this.jooq.set(jooq)
    }

    fun pojo(): GeneratorConfig {
        if (testContainer.isPresent && liveDb.isPresent) {
            throw GradleException("testContainer and liveDb are mutually exclusive")
        }

        if (!testContainer.isPresent && !liveDb.isPresent) {
            throw GradleException("Either testContainer or liveDb should be present in config")
        }

        return GeneratorConfig(
            db = if (testContainer.isPresent) testContainer.get().pojo() else liveDb.get().pojo(),
            flyway = flyway.get().pojo(),
            jooq = jooq.get().pojo()
        )
    }


    fun postgresTestContainer(version: String) {
        testContainer {
            className.set("org.testcontainers.containers.PostgreSQLContainer")
            name.set("postgres")
            this.version.set(version)
        }

        postgresCommon()
    }

    fun postgresLive(url: String, username: String, password: String) {
        liveDb {
            this.url.set(url)
            this.username.set(username)
            this.password.set(password)
        }

        postgresCommon()
    }

    private fun postgresCommon() {
        val extDepConfig = project.configurations.getByName("vertigramJooq")
        project.dependencies.add(extDepConfig.name, "org.testcontainers:postgresql:${Props.testContainersVersion}")
        project.dependencies.add(extDepConfig.name, "org.postgresql:postgresql:${Props.postgresqlDriverVersion}")
        project.dependencies.add(extDepConfig.name, "org.flywaydb:flyway-database-postgresql:${Props.flywayVersion}")
        jooq {
            driver.set("org.postgresql.Driver")
            dbName.set("org.jooq.meta.postgres.PostgresDatabase")
            includes.set(".*")
            inputSchema.set("public")
        }
    }
}