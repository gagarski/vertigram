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
            olderVersionsDir = layout.buildDirectory.dir("dokkaRepo/archive").get().asFile.absoluteFile
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

task("dokkaInitArchive") {
    doLast {
        layout.buildDirectory.dir("dokkaRepo/archive").get().asFile.absoluteFile.mkdirs()
    }
}

task("dokkaPull") {
    onlyIf { project.properties["vertigram.dokka.repo"] != null }
    dependsOn(":vertigram-dokka-tool:bootJar")
    dependsOn("dokkaInitArchive")
    doLast {
        javaexec {
            mainClass = "-jar"
            args(
                rootProject.rootDir.resolve("vertigram-dokka-tool/build/libs/vertigram-dokka-tool.jar"),
                "pull",
                "-local",
                layout.buildDirectory.dir("dokkaRepo").get().asFile.absoluteFile,
                "-remote",
                project.properties["vertigram.dokka.repo"]
            )
        }
    }
}



task("dokkaHousekeep") {
    onlyIf { project.properties["vertigram.dokka.repo"] != null }
    dependsOn(":vertigram-dokka-tool:bootJar")
    dependsOn("dokkaPull")
    doLast {
        javaexec {
            mainClass = "-jar"
            args(
                rootProject.rootDir.resolve("vertigram-dokka-tool/build/libs/vertigram-dokka-tool.jar"),
                "housekeep-versions",
                "-local",
                layout.buildDirectory.dir("dokkaRepo").get().asFile.absoluteFile,
                "-with-virtual",
                project.version.toString()
            )
        }
    }
}


tasks {
    named("dokkaGenerateModuleHtml") {
        dependsOn("dokkaHousekeep")
    }
    named("dokkaGeneratePublicationHtml") {
        dependsOn("dokkaHousekeep")
    }
}

task("dokkaInject") {
    onlyIf { project.properties["vertigram.dokka.repo"] != null }
    dependsOn(":vertigram-dokka-tool:bootJar")
    dependsOn("dokkaGenerate")
    doLast {
        javaexec {
            mainClass = "-jar"
            args(
                rootProject.rootDir.resolve("vertigram-dokka-tool/build/libs/vertigram-dokka-tool.jar"),
                "inject-new-version",
                "-local",
                layout.buildDirectory.dir("dokkaRepo").get().asFile.absoluteFile,
                "-new-path",
                layout.buildDirectory.dir("dokka/html").get().asFile.absoluteFile,
                "-new-name",
                project.version.toString(),

            )
        }
    }
}

task("dokkaPush") {
    onlyIf { project.properties["vertigram.dokka.repo"] != null }
    dependsOn(":vertigram-dokka-tool:bootJar")
    dependsOn("dokkaGenerate")
    dependsOn("dokkaInject")
    doLast {
        javaexec {
            mainClass = "-jar"
            args(
                rootProject.rootDir.resolve("vertigram-dokka-tool/build/libs/vertigram-dokka-tool.jar"),
                "push",
                "-local",
                layout.buildDirectory.dir("dokkaRepo").get().asFile.absoluteFile,
                "-remote",
                project.properties["vertigram.dokka.repo"]
            )
        }
    }
}