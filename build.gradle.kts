
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import java.time.LocalDate

group = "ski.gagar.vertigram"

repositories {
    mavenCentral()
}

plugins {
    id("org.jetbrains.dokka")
    signing
    alias(libsInternal.plugins.release)
    alias(libsInternal.plugins.nexus)
    alias(libsInternal.plugins.ssh)
}

dependencies {
    dokkaPlugin(libsInternal.dokka.versioning.plugin)
}

buildscript {
    dependencies {
        classpath(libsInternal.dokka.versioning.plugin)
    }

    repositories {
        mavenCentral()
    }
}

tasks.dokkaHtmlMultiModule.configure {
    moduleName.set("Vertigram")
    includes.from("README.md")

    pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        version = project.version as String
        olderVersionsDir = projectDir.resolve("build/oldDokka/archive")
    }
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "© ${LocalDate.now().year} <a href=\"https://github.com/gagarski/\">Kirill Gagarski</a>"
    }
}



nexusPublishing {
    repositories {
        sonatype()
    }
}


release {
    with(git) {
        requireBranch.set("gradle")
    }
}

tasks {
    named("afterReleaseBuild") {
        dependsOn(project.getSubprojects().map {
            "${it.name}:publish"
        })
    }
}

if (null != project.properties["vertigram.ssh.host"]) {
    apply(from = "./ssh.gradle")

    tasks.named("dokkaDownloadOld").configure {
        onlyIf { project.properties["vertigram.dokka.skipOld"] != "true" }
        dependsOn(":vertigram-dokka-tool:bootJar")
    }
    tasks.named("dokkaHtmlMultiModule").configure {
        dependsOn("dokkaDownloadOld")
    }

    tasks.register<Zip>("dokkaZip") {
        dependsOn("dokkaHtmlMultiModule")
        archiveFileName = "dokka.zip"
        from(layout.buildDirectory.dir("dokka/htmlMultiModule"))
    }

    tasks.named("dokkaUpload").configure {
        dependsOn("dokkaZip")
    }
}

