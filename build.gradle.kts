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


nexusPublishing {
    repositories {
        sonatype()
    }
}

release {
    with(git) {
        requireBranch.set("master")
    }
}

tasks {
    named("afterReleaseBuild") {
        dependsOn(project.getSubprojects().map {
            "${it.name}:publish"
        })
        dependsOn("vertigram-jooq-plugin:publishPlugins")

//        if (project.properties["vertigram.dokka.skip"] != "true") {
//            dependsOn("dokkaUpload")
//        }

        if (project.properties["vertigram.staging.close"] != "false") {
            dependsOn("closeAndReleaseRepository")
        }
    }
}