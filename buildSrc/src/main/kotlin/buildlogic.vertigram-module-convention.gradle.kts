import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

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
        jvmTarget.set(JvmTarget.JVM_22)
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
        freeCompilerArgs.add("-Xwarning-level=IDENTITY_SENSITIVE_OPERATIONS_WITH_VALUE_TYPE:disabled") // TODO remove me
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

java.sourceCompatibility = JavaVersion.VERSION_22
java.targetCompatibility = JavaVersion.VERSION_22

