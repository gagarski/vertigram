import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom
import java.time.LocalDate

fun Project.years(): String {
    val startYear = property("year.start")?.toString()
    val currentYear = LocalDate.now().year.toString()
    return if (null == startYear || startYear == currentYear)
            "${LocalDate.now().year}"
        else
            "$startYear–$currentYear"
}

fun MavenPom.vertigramPom(project: Project, description: String? = project.description) {
    name.set(project.name)
    this.description.set(description)
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
        url.set("https://github.com/gagarski/vertigram.git")
        connection.set("scm:git:git://github.com/gagarski/vertigram.git")
        developerConnection.set("scm:git:git://github.com/gagarski/vertigram.git")
    }
    issueManagement {
        url.set("https://github.com/gagarski/vertigram/issues")
    }
}
