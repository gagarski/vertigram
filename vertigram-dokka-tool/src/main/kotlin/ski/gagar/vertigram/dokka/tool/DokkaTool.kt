package ski.gagar.vertigram.dokka.tool

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.nio.file.Path

enum class Operation {
    REMOVE,
    PRINT_KEPT,
    PRINT_REMOVED
}

class Args {
    @Option(name = "-repository", required = true)
    lateinit var repository: Path
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

    @Argument
    var operation: Operation = Operation.PRINT_KEPT
        private set

    companion object {
        operator fun invoke(args: Array<String>) = Args().apply {
            CmdLineParser(this).parseArgument(*args)
        }
    }
}

fun main(args: Array<String>) {
    val parsedArgs = Args(args)
    val repo = Repository(
        root = parsedArgs.repository,
        majorVersionParts = parsedArgs.majorParts,
        keepMajor = parsedArgs.keepMajor,
        keepMinor = parsedArgs.keepMinor,
    )
    for (virtual in parsedArgs.virtual) {
        repo.addVirtual(virtual)
    }

    when (parsedArgs.operation) {
        Operation.PRINT_KEPT -> {
            for (kept in repo.getPathsToKeep()) {
                println(kept)
            }
        }
        Operation.PRINT_REMOVED -> {
            for (removed in repo.getPathsToRemove()) {
                println(removed)
            }
        }
        Operation.REMOVE -> {
            repo.removeOld()
        }
    }
}