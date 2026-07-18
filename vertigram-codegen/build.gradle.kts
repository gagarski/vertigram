plugins {
    id("buildlogic.vertigram-module-convention")

}

dependencies {
    api(libs.bundles.kotlin.std)
    implementation(project(":vertigram-annotations"))
    api(libsInternal.bundles.kotlinpoet)
    api(libsInternal.ksp.api)
}

description = "KSP code generators for Vertigram modules."
