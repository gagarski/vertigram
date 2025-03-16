package ski.gagar.vertigram.dokka.tool.operations

import com.github.fracpete.processoutput4j.output.StreamingProcessOutput
import com.github.fracpete.rsync4j.RSync
import org.kohsuke.args4j.Option
import ski.gagar.vertigram.dokka.tool.output.Output
import ski.gagar.vertigram.dokka.tool.withTrailingSeparator
import java.nio.file.Path

class Push : Operation {
    @Option(name = "-local", required = true)
    lateinit var local: Path
        private set

    @Option(name = "-remote", required = true)
    lateinit var remote: String
        private set

    override fun perform() {
        val rsync = RSync()
            .source(local.toString().withTrailingSeparator())
            .destination(remote)
            .delete(true)
            .recursive(true)
            .compress(true)
            .progress(true)
            .stats(true)
            .rsh("ssh")
            .chmod("Du=rwx,Dg=rx,Do=rx,Fu=rw,Fg=r,Fo=r")

        val out = StreamingProcessOutput(Output())
        out.monitor(rsync.builder())

        val res = rsync.execute()
        if (res.exitCode != 0) {
            throw IllegalStateException("non-zero exit code: ${res.exitCode}")
        }
    }
}