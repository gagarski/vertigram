
plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.dokka-convention")
    `maven-publish`
    `kotlin-dsl`
    alias(libsInternal.plugins.gradle.publish)
}

gradlePlugin {
    website = "https://vertigram.gagar.ski"
    vcsUrl = "https://github.com/gagarski/vertigram"
    // Define the plugin
    plugins.create("vertigramJooq") {
        id = "ski.gagar.vertigram.jooq"
        displayName = "Vertigram jOOQ plugin"
        description = "Vertigram plugin that allows to execute jOOQ code generation together with Flyway " +
                "migrations and optionally involving testcontainers"
        tags = listOf("vertigram", "jooq", "flyway", "testcontainers", "vertx")
        implementationClass = "ski.gagar.vertigram.jooq.gradle.VertigramJooqPlugin"
    }
}


dependencies {
    implementation(libs.bundles.kotlin.std)
    implementation(project(":vertigram-jooq-app-api"))
    implementation(libs.bundles.jackson)
}

val generateResources = tasks.create("generateResources") {
    val propFile = file("${layout.buildDirectory.get()}/generated/vertigram-jooq.properties")
    outputs.file(propFile)
    doLast {
        mkdir(propFile.parentFile)
        propFile.writeText("""
            version=${project.version}
            test-containers.version=${libs.versions.testcontainers.get()}
            postgresql-driver.version=${libs.versions.postgresql.get()}
            flyway.version=${libs.versions.flyway.get()}
        """.trimIndent())
    }
}

tasks.withType<ProcessResources> {
    from(files(generateResources))
}
