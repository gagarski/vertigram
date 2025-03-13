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
            name.set(project.name)
            description.set(project.description)
            url.set("https://vertigram.gagar.ski/")
            licenses {
                license {
                    name.set("Apache License, Version 2.0")
                    url.set("https://opensource.org/licenses/Apache-2.0")
                }
            }
            developers {
                developer {
                    name.set("Kirill Gagarski")
                    email.set("kirill.gagarski@gmail.com")
                }
            }
            scm {
                url.set(
                    "https://github.com/gagarski/vertigram.git"
                )
                connection.set(
                    "scm:git:git://github.com/gagarski/vertigram.git"
                )
                developerConnection.set(
                    "scm:git:git://github.com/gagarski/vertigram.git"
                )
            }
            issueManagement {
                url.set("https://github.com/gagarski/vertigram/issues")
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}