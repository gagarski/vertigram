
plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.dokka-convention")
    id("buildlogic.maven-central-convention")
    `kotlin-dsl`
    `maven-publish`
    signing
}

description = "Vertigram plugin that allows to execute jOOQ code generation together with Flyway migrations"

gradlePlugin {
    plugins.create("vertigramJooq") {
        id = "ski.gagar.vertigram.jooq"
        implementationClass = "ski.gagar.vertigram.jooq.gradle.VertigramJooqPlugin"
    }
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            vertigramPom(project, providers.provider {
                project.description?.takeIf { it.isNotBlank() }
                    ?: throw GradleException("Project ${project.path} must define a non-empty description for publishing.")
            })
        }
    }
}

signing {
    val signingKeyId = findProperty("signingKeyId") as String?
    val signingKey = findProperty("signingKey") as String?
    val signingPassword = findProperty("signingPassword") as String?

    if (signingKey != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    }

    publishing.publications.withType<MavenPublication>().configureEach {
        sign(this)
    }
}

val runnerDependencies by configurations.creating {
    description = "Dependencies used only by the forked Vertigram jOOQ runner."
    isCanBeConsumed = false
    isCanBeResolved = false
}

configurations.named("compileOnly") {
    extendsFrom(runnerDependencies)
}

dependencies {
    implementation(libs.jackson.databind)

    runnerDependencies(libs.kotlin.stdlib)
    runnerDependencies(libs.bundles.jackson)
    runnerDependencies(libs.bundles.testcontainers)
    runnerDependencies(libs.bundles.hikari)
    runnerDependencies(libs.bundles.flyway)
    runnerDependencies(libs.bundles.slf4j.api)
    runnerDependencies(libs.bundles.logback)
    runnerDependencies(libs.bundles.jooq.codegen)
    runnerDependencies(libs.jackson.dataformat.xml)
}

val runnerDependencyCoordinates = providers.provider {
    runnerDependencies.dependencies.map {
        "${it.group}:${it.name}:${it.version}"
    }.sorted()
}

val generateResources = tasks.register("generateResources") {
    val propFile = file("${layout.buildDirectory.get()}/generated/vertigram-jooq.properties")
    outputs.file(propFile)
    inputs.property("runnerDependencies", runnerDependencyCoordinates)
    doLast {
        mkdir(propFile.parentFile)
        propFile.writeText("""
            runner-dependencies=${runnerDependencyCoordinates.get().joinToString(",")}
            test-containers.version=${libs.versions.testcontainers.get()}
            postgresql-driver.version=${libs.versions.postgresql.get()}
            flyway.version=${libs.versions.flyway.get()}
        """.trimIndent())
    }
}

tasks.withType<ProcessResources> {
    from(files(generateResources))
}
