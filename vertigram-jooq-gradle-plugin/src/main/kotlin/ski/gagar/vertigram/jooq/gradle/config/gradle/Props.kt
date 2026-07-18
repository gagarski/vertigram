package ski.gagar.vertigram.jooq.gradle.config.gradle

import java.util.*

object Props {
    private val props = Properties()
    init {
        props.load(javaClass.classLoader.getResourceAsStream("vertigram-jooq.properties"))
    }

    val runnerDependencies = props.getProperty("runner-dependencies")
        .split(',')
        .filter(String::isNotBlank)

    val testContainersVersion = props.getProperty("test-containers.version")
    val postgresqlDriverVersion = props.getProperty("postgresql-driver.version")
    val flywayVersion = props.getProperty("flyway.version")
}