package ski.gagar.vertigram.dokka.tool.operations

import org.kohsuke.args4j.Option
import java.nio.file.Path

class InjectNewVersion : Operation {
    @Option(name = "-local", required = true)
    lateinit var local: Path
        private set
    @Option(name = "-new-path", required = true)
    lateinit var newPath: Path
        private set
    @Option(name = "-new-name", required = true)
    lateinit var newName: String
        private set

    override fun perform() {
        val latest = local.resolve("latest")
        latest.toFile().deleteRecursively()
        latest.toFile().mkdirs()
        newPath.toFile().copyRecursively(latest.toFile())


        val arch = local.resolve("archive").resolve(newName)
        arch.toFile().deleteRecursively()
        arch.toFile().mkdirs()
        newPath.toFile().copyRecursively(arch.toFile())

        arch.resolve("older").toFile().deleteRecursively()
    }

}