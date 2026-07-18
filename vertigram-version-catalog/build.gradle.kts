group = "ski.gagar.vertigram"

repositories {
    mavenLocal()
    mavenCentral()
}

plugins {
    `version-catalog`
    `maven-publish`
    id("buildlogic.maven-central-convention")
    signing
}

val excludedSubprojects = setOf(
    "vertigram-bom",
    "vertigram-version-catalog",
    "vertigram-codegen",
    "vertigram-jooq-gradle-plugin",
    "vertigram-annotations"
)

catalog {
    versionCatalog {
        from(files("${rootDir}/gradle/libs.versions.toml"))

        val vertigramAll = mutableListOf<String>()

        for (subproject in project.rootProject.subprojects) {
            if (subproject.name in excludedSubprojects)
                continue
            library(subproject.name, "${subproject.group}:${subproject.name}:${subproject.version}")
            vertigramAll.add(subproject.name)
        }

        bundle("vertigram-all", vertigramAll)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["versionCatalog"])

            pom {
                vertigramPom(project, "Gradle version catalog for Vertigram")
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
