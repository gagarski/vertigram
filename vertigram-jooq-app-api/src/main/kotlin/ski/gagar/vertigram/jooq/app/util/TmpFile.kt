package ski.gagar.vertigram.jooq.app.util

import java.io.Closeable
import java.io.File

class TmpFile : Closeable {
    val file: File = File.createTempFile("aybo", null)
    override fun close() {
        file.delete()
    }
}

fun withTmpFile(block: (file: File) -> Unit) {
    TmpFile().use {
        block(it.file)
    }
}
