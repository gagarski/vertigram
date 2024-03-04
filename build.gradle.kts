
import org.jetbrains.dokka.versioning.VersioningPlugin
import org.jetbrains.dokka.versioning.VersioningConfiguration

group = "ski.gagar.vertigram"
version = "1.0.0-alpha42-SNAPSHOT"

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
        olderVersionsDir = projectDir.resolve("tmp")

    }
}

nexusPublishing {
    repositories {
        sonatype()
    }
}

release {
    git {
        requireBranch.set(null)
    }
}