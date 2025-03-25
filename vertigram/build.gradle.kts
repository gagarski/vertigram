plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.dokka-convention")
    id("buildlogic.maven-publishing-convention")
}

dependencies {
    api(libs.bundles.jackson)
    api(libs.bundles.vertx.core)
    api(libs.bundles.vertx.web)
    api(libs.bundles.vertx.web.client)
    api(libs.bundles.commons.lang3)
    api(libs.bundles.reflections)
    api(libs.bundles.slf4j.api)

    api(project(":vertigram-telegram-client"))
    api(project(":vertigram-core"))
    api(project(":vertigram-annotations"))
    implementation(libs.bundles.logback)

}

description = "Vertigram"
