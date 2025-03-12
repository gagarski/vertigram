plugins {
    id("buildlogic.vertigram-module-convention")
    id("buildlogic.dokka-convention")
}

dependencies {
    api(libs.bundles.kotlin.std)
}

description = "Vertigram Annotations"
