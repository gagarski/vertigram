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


nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}

release {
    with(git) {
        requireBranch.set("master")
    }
}

gradle.projectsEvaluated {
    val publishToSonatypeTasks = subprojects.mapNotNull {
        it.tasks.findByName("publishToSonatype")
    }

    tasks {
        named("closeAndReleaseSonatypeStagingRepository") {
            mustRunAfter(publishToSonatypeTasks)
        }

        named("afterReleaseBuild") {
            dependsOn(publishToSonatypeTasks)

            if (providers.gradleProperty("vertigram.dokka.publish").orNull != "false") {
                dependsOn(":vertigram-docs:dokkaPush")
            }

            if (providers.gradleProperty("vertigram.staging.close").orNull == "true") {
                dependsOn("closeAndReleaseSonatypeStagingRepository")
            }
        }
    }

    project(":vertigram-docs").tasks {
        named("dokkaPush") {
            mustRunAfter(publishToSonatypeTasks)
        }
    }
}
