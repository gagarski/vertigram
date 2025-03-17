import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "ski.gagar.vertigram"

plugins {
    alias(libsInternal.plugins.spring.boot)
    java
    kotlin("jvm")
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libsInternal.rsync4j)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

springBoot {
    mainClass = "ski.gagar.vertigram.dokka.tool.DokkaToolKt"
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName = "vertigram-dokka-tool.jar"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libsInternal.args4j)

    testImplementation(libsInternal.junit.jupiter)
}
