plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-publishing-convention")
}

dependencies {
    implementation(libs.bundles.kotlin.std)
    implementation(libs.jackson.annotations)
}

description = "API for interacting between vertigram-jooq-gradle-plugin and vertigram-jooq-app"

tasks.matching { it.name.startsWith("dokka") }.configureEach {
    onlyIf { false }
}
