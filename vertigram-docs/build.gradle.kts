plugins {
    id("org.jetbrains.dokka")
    id("buildlogic.dokka-convention")
}

group = "ski.gagar.vertigram"

dependencies {
    dokka(project(":vertigram-util"))
    dokka(project(":vertigram-telegram-client"))
    dokka(project(":vertigram-core"))
    dokka(project(":vertigram"))
    dokka(project(":vertigram-logback"))
    dokka(project(":vertigram-jooq"))
    dokka(project(":vertigram-jooq-gradle-plugin"))
    dokkaHtmlPlugin("org.jetbrains.dokka:versioning-plugin")
}

dokka {
    pluginsConfiguration {
        versioning {
            version = project.version as String
            val old = rootProject.projectDir.resolve("build/dokkaRepo/archive")
            if (old.exists()) olderVersionsDir = old
        }
        dokkaSourceSets.create("main") {
            includes.from("README.md")
        }
        html {
            moduleName.set("Vertigram")

            footerMessage = "© ${years()} <a class=\"footer--link footer--link_external\" href=\"https://github.com/gagarski/\">Kirill Gagarski</a>"
        }
        dokkaPublications.html {
            includes.from("README.md")
        }
    }
}