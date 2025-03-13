import org.gradle.api.Project
import java.time.LocalDate

fun Project.years(): String {
    val startYear = property("year.start").toString()
    val currrentYear = LocalDate.now().year.toString()
    return if (null == startYear || startYear == currrentYear)
            "${LocalDate.now().year}"
        else
            "$startYear–$currrentYear"
}