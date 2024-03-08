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
    implementation(libs.bundles.testcontainers)
    implementation(libs.bundles.jackson)
}