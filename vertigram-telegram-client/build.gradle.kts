plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-publishing-convention")
    id("buildlogic.dokka-convention")
    alias(libsInternal.plugins.ksp)
}

dependencies {
    api(libs.bundles.commons.lang3)
    api(libs.bundles.jackson)
    api(libs.bundles.vertx.core)
    api(libs.bundles.vertx.web)
    api(libs.bundles.vertx.web.client)
    api(libs.bundles.kotlin.std)
    api(libs.bundles.kotlinx.html)
    api(libs.bundles.reflections)
    api(libs.bundles.slf4j.api)
    api(project(":vertigram-util"))
    api(project(":vertigram-annotations"))
    ksp(project(":vertigram-codegen"))
}

description = "Telegram Bot API client types and methods for Vertigram."

tasks.named<Jar>("sourcesJar").configure {
    dependsOn("kspKotlin")
}

tasks.matching { it.name.startsWith("dokka") }.configureEach {
    dependsOn("kspKotlin")
}
