
import net.researchgate.release.ReleaseExtension
import org.jetbrains.dokka.versioning.VersioningPlugin
import org.jetbrains.dokka.versioning.VersioningConfiguration

group = "ski.gagar.vertigram"

repositories {
    mavenCentral()
}

plugins {
    id("org.jetbrains.dokka")
    signing
    alias(libs.plugins.release)
    alias(libs.plugins.nexus)
}

dependencies {
    dokkaPlugin(libs.dokka.versioning.plugin)
}

buildscript {
    dependencies {
        classpath(libs.dokka.versioning.plugin)
    }

    repositories {
        mavenCentral()
    }
}

tasks.dokkaHtmlMultiModule.configure {
    moduleName.set("Vertigram")
    pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        version = "1.0"
        olderVersionsDir = projectDir.resolve("build/dokka-old")
    }
//    removeChildTasks([
//        project(":compose:runtime"),
//    ])
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