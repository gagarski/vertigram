import org.gradle.api.Project
import java.time.LocalDate

fun Project.years(): String {
    val startYear = property("year.start")?.toString()
    val currentYear = LocalDate.now().year.toString()
    return if (null == startYear || startYear == currentYear)
            "${LocalDate.now().year}"
        else
            "$startYear–$currentYear"
}