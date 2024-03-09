
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin

group = "ski.gagar.vertigram"

repositories {
    mavenCentral()
}

plugins {
    id("org.jetbrains.dokka")
    signing
    alias(libsInternal.plugins.release)
    alias(libsInternal.plugins.nexus)
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
    pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        version = "1.0"
        olderVersionsDir = projectDir.resolve("build/dokka-old")
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