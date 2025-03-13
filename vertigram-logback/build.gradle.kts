plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-publishing-convention")
    id("buildlogic.dokka-convention")
}

dependencies {
    api(libs.bundles.vertx.core)
    api(libs.bundles.kotlin.std)
    api(libs.bundles.logback)

    api(project(":vertigram"))
    api(project(":vertigram-telegram-client"))
}

description = "Vertigram Logback"
