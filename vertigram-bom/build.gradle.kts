group = "ski.gagar.vertigram"

repositories {
    mavenLocal()
    mavenCentral()
}

plugins {
    `java-platform`
    `maven-publish`
    id("buildlogic.maven-central-convention")
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
            api(project.dependencies.project(mapOf("path" to subproject.path)))
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
            from(components["javaPlatform"])

            pom {
                vertigramPom(project, "Dependency management BOM for Vertigram")
            }
        }
    }
}

signing {
    val signingKeyId = findProperty("signingKeyId") as String?
    val signingKey = findProperty("signingKey") as String?
    val signingPassword = findProperty("signingPassword") as String?

    if (signingKey != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    }

    sign(publishing.publications["maven"])
}
