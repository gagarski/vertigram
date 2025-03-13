import org.gradle.api.Project
import java.time.LocalDate

fun Project.years(): String {
    val startYear = property("year.start")
    val currrentYear = LocalDate.now().year
    return if (null == startYear || startYear == currrentYear)
            "${LocalDate.now().year}"
        else
            "$startYear–$currrentYear"
}