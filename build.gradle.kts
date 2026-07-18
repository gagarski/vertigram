group = "ski.gagar.vertigram"

repositories {
    mavenCentral()
}

plugins {
    id("org.jetbrains.dokka")
    signing
    alias(libsInternal.plugins.release)
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


release {
    with(git) {
        requireBranch.set("master")
    }
}

gradle.projectsEvaluated {
    val publishToMavenCentralTasks = subprojects.mapNotNull {
        it.tasks.findByName("publishAllPublicationsToMavenCentralRepository")
    }

    tasks {
        named("afterReleaseBuild") {
            dependsOn(publishToMavenCentralTasks)

            if (providers.gradleProperty("vertigram.dokka.publish").orNull != "false") {
                dependsOn(":vertigram-docs:dokkaPush")
            }

            when (val deploymentAction = providers.gradleProperty("vertigram.central.deployment.action").orNull ?: "upload") {
                "upload" -> {}
                "validate" -> {}
                "publish" -> {}
                else -> throw GradleException(
                    "Unsupported vertigram.central.deployment.action=$deploymentAction. " +
                        "Expected one of: upload, validate, publish."
                )
            }
        }
    }

    project(":vertigram-docs").tasks {
        named("dokkaPush") {
            mustRunAfter(publishToMavenCentralTasks)
        }
    }
}
