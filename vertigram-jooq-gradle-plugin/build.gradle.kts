
plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.dokka-convention")
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
            vertigramPom(project)
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

dependencies {
    implementation(libs.bundles.kotlin.std)
    implementation(project(":vertigram-jooq-app-api"))
    implementation(project(":vertigram-jooq-app"))
    implementation(libs.bundles.jackson)
}

val version = project.version

val generateResources = tasks.register("generateResources") {
    val propFile = file("${layout.buildDirectory.get()}/generated/vertigram-jooq.properties")
    outputs.file(propFile)
    doLast {
        mkdir(propFile.parentFile)
        propFile.writeText("""
            version=${version}
            test-containers.version=${libs.versions.testcontainers.get()}
            postgresql-driver.version=${libs.versions.postgresql.get()}
            flyway.version=${libs.versions.flyway.get()}
        """.trimIndent())
    }
}

tasks.withType<ProcessResources> {
    from(files(generateResources))
}
