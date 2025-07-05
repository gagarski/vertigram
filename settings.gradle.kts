rootProject.name = "vertigram-all"

include(":")
include(":vertigram-util")
include(":vertigram-annotations")
include(":vertigram-codegen")
include(":vertigram-telegram-client")
include(":vertigram-core")
include(":vertigram")
include(":vertigram-logback")
include(":vertigram-jooq")
include("vertigram-jooq-gradle-plugin")
include("vertigram-version-catalog")
include("vertigram-bom")
include("vertigram-jooq-app-api")
include("vertigram-jooq-app")
include("vertigram-dokka-tool")
include("vertigram-docs")

dependencyResolutionManagement {
    versionCatalogs {
        create("libsInternal") {
            from(files("./gradle/libs.internal.versions.toml"))
        }
    }
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}
