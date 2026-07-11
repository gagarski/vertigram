import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "ski.gagar.vertigram"

plugins {
    `maven-publish`
    signing
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])

        pom {
            vertigramPom(project, providers.provider {
                project.description?.takeIf { it.isNotBlank() }
                    ?: throw GradleException("Project ${project.path} must define a non-empty description for publishing.")
            })
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
