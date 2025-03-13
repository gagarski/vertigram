import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "ski.gagar.vertigram"

val catalogs = extensions
    .getByType<VersionCatalogsExtension>()

val libs = catalogs.named("libs")
val libsInternal = catalogs.named("libsInternal")

plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenLocal()
    mavenCentral()
    gradlePluginPortal()
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

publishing {
    if (name == "vertigram-jooq-gradle-plugin")
        return@publishing

    publications.create<MavenPublication>("maven") {
        from(components["java"])

        pom {
            name.set(project.name)
            description.set(project.description)
            url.set("https://vertigram.gagar.ski/")
            licenses {
                license {
                    name.set("Apache License, Version 2.0")
                    url.set("https://opensource.org/licenses/Apache-2.0")
                }
            }
            developers {
                developer {
                    name.set("Kirill Gagarski")
                    email.set("kirill.gagarski@gmail.com")
                }
            }
            scm {
                url.set(
                    "https://github.com/gagarski/vertigram.git"
                )
                connection.set(
                    "scm:git:git://github.com/gagarski/vertigram.git"
                )
                developerConnection.set(
                    "scm:git:git://github.com/gagarski/vertigram.git"
                )
            }
            issueManagement {
                url.set("https://github.com/gagarski/vertigram/issues")
            }
        }
    }
}

signing {
    if (name == "vertigram-jooq-gradle-plugin")
        return@signing

    sign(publishing.publications["maven"])
}