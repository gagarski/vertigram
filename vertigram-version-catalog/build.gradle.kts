group = "ski.gagar.vertigram"

repositories {
    mavenLocal()
    mavenCentral()
}

plugins {
    `version-catalog`
    `maven-publish`
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
        }
    }
}