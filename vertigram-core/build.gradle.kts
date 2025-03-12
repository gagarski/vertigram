plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.dokka-convention")
}

dependencies {
    api(libs.bundles.jackson)
    api(libs.bundles.vertx.core)
    api(libs.bundles.commons.lang3)
    api(libs.bundles.kotlin.std)
    api(libs.bundles.slf4j.api)
    api(project(":vertigram-util"))
}

description = "Vertigram Core"
