import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("buildlogic.vertigram-module")
}

dependencies {
    implementation(libs.bundles.kotlin.std)
    implementation(libs.jackson.annotations)


    testImplementation(libs.junit.api)
    testRuntimeOnly (libs.junit.engine)

    dokkaPlugin(libsInternal.dokka.versioning.plugin)
}

description = "API for interacting between vertigram-jooq-gradle-plugin and vertigram-jooq-app"

tasks.withType<DokkaTaskPartial>().configureEach {
    onlyIf { false }
}