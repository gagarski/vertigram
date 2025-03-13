import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-publishing-convention")
    id("buildlogic.dokka-convention")
}

dependencies {
    implementation(libs.bundles.jackson)
    implementation(project(":vertigram-jooq-app-api"))
    implementation(libs.bundles.testcontainers)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.hikari)
    implementation(libs.bundles.flyway)
    implementation(libs.bundles.slf4j.api)
    implementation(libs.bundles.logback)
    implementation(libs.bundles.jooq.codegen)
    implementation(libs.jackson.dataformat.xml)
}

description = "Vertigram jOOQ app"
