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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java.sourceCompatibility = JavaVersion.VERSION_21

springBoot {
    mainClass = "ski.gagar.vertigram.dokka.tool.DokkaToolKt"
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName = "vertigram-dokka-tool.jar"
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libsInternal.args4j)

    testImplementation(libsInternal.junit.api)
    testRuntimeOnly (libsInternal.junit.engine)
}