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
        requireBranch.set("revive-2026-07")
    }
}

gradle.projectsEvaluated {
    val publishToSonatypeTasks = subprojects.mapNotNull {
        it.tasks.findByName("publishToSonatype")
    }

    tasks {
        named("closeSonatypeStagingRepository") {
            mustRunAfter(publishToSonatypeTasks)
        }

        named("closeAndReleaseSonatypeStagingRepository") {
            mustRunAfter(publishToSonatypeTasks)
        }

        named("afterReleaseBuild") {
            dependsOn(publishToSonatypeTasks)

            if (providers.gradleProperty("vertigram.dokka.publish").orNull != "false") {
                dependsOn(":vertigram-docs:dokkaPush")
            }

            when (val stagingAction = providers.gradleProperty("vertigram.staging.action").orNull ?: "none") {
                "none" -> {}
                "close" -> dependsOn("closeSonatypeStagingRepository")
                "close-and-release" -> dependsOn("closeAndReleaseSonatypeStagingRepository")
                else -> throw GradleException(
                    "Unsupported vertigram.staging.action=$stagingAction. " +
                        "Expected one of: none, close, close-and-release."
                )
            }
        }
    }

    project(":vertigram-docs").tasks {
        named("dokkaPush") {
            mustRunAfter(publishToSonatypeTasks)
        }
    }
}
