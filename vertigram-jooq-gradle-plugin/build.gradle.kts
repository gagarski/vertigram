
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
        propFile.writeText("version=${project.version}")
    }
}

tasks.withType<ProcessResources> {
    from(files(generateResources))
}