plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.maven-central-java-convention")
}

dependencies {
    api(libs.bundles.kotlin.std)
    api(project(":vertigram-annotations"))
    api(libsInternal.bundles.kotlinpoet)
    api(libsInternal.ksp.api)
}

description = "KSP code generators for Vertigram modules."
