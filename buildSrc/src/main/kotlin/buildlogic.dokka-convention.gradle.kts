
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.io.ByteArrayOutputStream
import java.net.URI

group = "ski.gagar.vertigram"

val catalogs = extensions
    .getByType<VersionCatalogsExtension>()

val libs = catalogs.named("libs")

plugins {
    id("org.jetbrains.dokka")
}


val dokkaSourceSetClasspath: Configuration by configurations.creating {
    description = "Declare additional DokkaSourceSet dependencies."
    isCanBeConsumed = false
    isCanBeResolved = false
}

val dokkaSourceSetClasspathResolver: Configuration by configurations.creating {
    description = "Resolves the additional DokkaSourceSet dependencies."
    extendsFrom(dokkaSourceSetClasspath)
    isCanBeConsumed = false
    isCanBeResolved = true
    isTransitive = false // be defensive, try to only add a bare minimum of additional classes to the Dokka source set.
}


dependencies {
    for (sub in rootProject.subprojects) {
        if (sub.name == "vertigram-docs")
            continue
        if (sub.name == name)
            continue
        dokkaSourceSetClasspath(project(":${sub.name}"))
    }
}

dokka {
    pluginsConfiguration.html {
        footerMessage.set("© ${years()} <a href=\"https://github.com/gagarski/\">Kirill Gagarski</a>")
    }
    dokkaSourceSets.configureEach {
        classpath.from(dokkaSourceSetClasspathResolver)
        sourceRoots.from(file("src/main/"), file("build/generated/source/ksp/main/kotlin"))
        includes.from("README.md")
        suppressGeneratedFiles = false

        jdkVersion = 21

        documentedVisibilities.set(setOf(
            VisibilityModifier.Public,
            VisibilityModifier.Protected
        ))
        perPackageOption {
            matchingRegex.set(".*internal.*")
            suppress.set(true)
        }

        perPackageOption {
            matchingRegex.set(".*samples.*")
            suppress.set(true)
        }
        samples.from(files("src/main/kotlin/ski/gagar/vertigram/samples/Samples.kt"))
        sourceLink {
            val isSnapshot = version.toString().split("-").let { it[it.lastIndex] } == "SNAPSHOT"
            val urlVersion = if (isSnapshot) {
                val os = ByteArrayOutputStream()
                exec {
                    workingDir = rootDir
                    commandLine("git", "rev-parse", "--short", "HEAD")

                    standardOutput = os
                }
                os.toString().trim()
            } else {
                "v${version}"
            }
            // Unix based directory relative path to the root of the project (where you execute gradle respectively).
            localDirectory.set(rootDir)

            // URL showing where the source code can be accessed through the web browser
            remoteUrl.set(URI("https://github.com/gagarski/vertigram/tree/${urlVersion}"))
            // Suffix which is used to append the line number to the URL. Use #L for GitHub
            remoteLineSuffix.set("#L")
            externalDocumentationLinks.register("kotlinx-coroutines") {
                url.set(URI("https://kotlinlang.org/api/kotlinx.coroutines/"))
            }
            externalDocumentationLinks.register("vertx") {
                url.set(URI("https://vertx.io/docs/${libs.findVersion("vertx").get()}/apidocs/"))
            }
            val jacksonVersion = libs.findVersion("jackson").get()

            externalDocumentationLinks.register("jackson-annotations") {
              url.set(URI("https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-annotations/${jacksonVersion}/"))
            }
            externalDocumentationLinks.register("jackson-core") {
                url.set(URI("https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-core/${jacksonVersion}/"))
            }
            externalDocumentationLinks.register("jackson-databind") {
                url.set(URI("https://javadoc.io/doc/com.fasterxml.jackson.core/jackson-databind/${jacksonVersion}/"))
            }
            externalDocumentationLinks.register("jooq") {
                url.set(URI("https://javadoc.io/doc/org.jooq/jooq/${libs.findVersion("jooq").get()}"))
                packageListUrl.set(URI("https://javadoc.io/doc/org.jooq/jooq/${libs.findVersion("jooq").get()}/element-list"))
            }
        }
    }
}
