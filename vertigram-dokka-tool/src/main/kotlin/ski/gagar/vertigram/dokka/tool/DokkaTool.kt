package ski.gagar.vertigram.dokka.tool

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.spi.SubCommand
import org.kohsuke.args4j.spi.SubCommandHandler
import org.kohsuke.args4j.spi.SubCommands
import ski.gagar.vertigram.dokka.tool.operations.*


class Args {
    @Argument(required = true, handler = SubCommandHandler::class, metaVar = "command")
    @SubCommands(
        SubCommand(name = "pull", impl = Pull::class),
        SubCommand(name = "housekeep-versions", impl = HousekeepVersions::class),
        SubCommand(name = "inject-new-version", impl = InjectNewVersion::class),
        SubCommand(name = "push", impl = Push::class),
    )
    lateinit var operation: Operation
        private set

    companion object {
        operator fun invoke(args: Array<String>) = Args().apply {
            CmdLineParser(this).parseArgument(*args)
        }
    }
}

fun main(args: Array<String>) {
    Args(args).operation.perform()
}