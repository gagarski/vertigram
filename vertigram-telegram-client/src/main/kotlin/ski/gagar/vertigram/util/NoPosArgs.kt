package ski.gagar.vertigram.util

/**
 * A barrier to forbid positional args usage in function/constructor signature.
 */
class NoPosArgs private constructor() {
    companion object {
        @PublishedApi
        internal val INSTANCE = NoPosArgs()
    }

    override fun toString(): String = "NoPosArgs"
}

