package ski.gagar.vertigram.dokka.tool.output

import com.github.fracpete.processoutput4j.core.StreamingProcessOutputType
import com.github.fracpete.processoutput4j.core.StreamingProcessOwner


class Output : StreamingProcessOwner {
    override fun getOutputType(): StreamingProcessOutputType {
        return StreamingProcessOutputType.BOTH
    }

    override fun processOutput(line: String, stdout: Boolean) {
        println((if (stdout) "[OUT] " else "[ERR] ") + line)
    }
}