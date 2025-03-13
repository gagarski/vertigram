import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-publishing-convention")
}

dependencies {
    implementation(libs.bundles.kotlin.std)
    implementation(libs.jackson.annotations)
}

description = "API for interacting between vertigram-jooq-gradle-plugin and vertigram-jooq-app"

tasks.withType<DokkaTaskPartial>().configureEach {
    onlyIf { false }
}