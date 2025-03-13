plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-publishing-convention")
    id("buildlogic.dokka-convention")
}

dependencies {
    api(libs.bundles.jackson)
    api(libs.bundles.vertx.core)
    api(libs.bundles.kotlin.std)
    api(libs.bundles.slf4j.api)
}

description = "Vertigram Util"
