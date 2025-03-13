plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-publishing-convention")
    id("buildlogic.dokka-convention")
}

dependencies {
    api(libs.bundles.hikari)
    api(libs.bundles.vertx.core)
    api(libs.bundles.vertx.jdbc)
    api(libs.bundles.flyway)
    api(libs.bundles.kotlin.std)
    api(libs.bundles.jooq)
    api(libs.bundles.slf4j.api)

    api(project(":vertigram-util"))

    testImplementation(libsInternal.junit.jupiter)

    dokkaPlugin(libsInternal.dokka.versioning.plugin)
}

description = "Vertigram jOOQ"
