
plugins {
    id("buildlogic.vertigram-module")
    `kotlin-dsl`
}



gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = "ski.gagar.vertigram.jooq"
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