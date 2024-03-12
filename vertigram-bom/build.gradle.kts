group = "ski.gagar.vertigram"

repositories {
    mavenLocal()
    mavenCentral()
}

plugins {
    `java-platform`
    `maven-publish`
    signing
}

val subprojectsExcludedFromBom = setOf(
    "vertigram-bom",
    "vertigram-version-catalog",
    "vertigram-jooq-gradle-plugin",
    "vertigram-codegen",
    "vertigram-annotations",
    "vertigram-dokka-tool",
    "vertigram-jooq-app",
    "vertigram-jooq-app-api"
)

dependencies {
    constraints {
        for (subproject in project.rootProject.subprojects) {
            if (subproject.name in subprojectsExcludedFromBom)
                continue
            api(subproject)
        }

        val catalog = versionCatalogs.named("libs")

        for (alias in catalog.libraryAliases) {
            api(catalog.findLibrary(alias).get().get())
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            afterEvaluate { from(components["javaPlatform"]) }
        }
    }
}