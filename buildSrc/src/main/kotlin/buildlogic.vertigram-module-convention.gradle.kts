import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "ski.gagar.vertigram"

val catalogs = extensions
    .getByType<VersionCatalogsExtension>()

val libsInternal = catalogs.named("libsInternal")

plugins {
    kotlin("jvm")
    `java-library`
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }
}

dependencies {
    testImplementation(libsInternal.findLibrary("junit-jupiter").get().get().toString())
    testRuntimeOnly(libsInternal.findLibrary("junit-platform-launcher").get().get().toString())
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

