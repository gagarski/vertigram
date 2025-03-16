package ski.gagar.vertigram.dokka.tool.operations

import org.kohsuke.args4j.Option
import ski.gagar.vertigram.dokka.tool.VersionManager
import java.nio.file.Path

class HousekeepVersions : Operation {
    @Option(name = "-local", required = true)
    lateinit var local: String
        private set
    @Option(name = "-keep-major")
    var keepMajor: Int = 3
        private set

    @Option(name = "-keep-minor")
    var keepMinor: Int = 1
        private set

    @Option(name = "-major-parts")
    var majorParts: Int = 2
        private set

    @Option(name = "-with-virtual")
    var virtual: List<String> = mutableListOf()
        private set

    override fun perform() {
        val repo = VersionManager(
            root = Path.of(local),
            majorVersionParts = majorParts,
            keepMajor = keepMajor,
            keepMinor = keepMinor,
        )
        for (v in virtual) {
            repo.addVirtual(v)
        }
        repo.removeOld()
    }
}