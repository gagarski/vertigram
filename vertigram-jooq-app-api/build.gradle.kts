plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-central-java-convention")
}

dependencies {
    implementation(libs.bundles.kotlin.std)
    implementation(libs.jackson.annotations)
}

description = "Public API for Vertigram jOOQ application support."

tasks.matching { it.name.startsWith("dokka") }.configureEach {
    onlyIf { false }
}
