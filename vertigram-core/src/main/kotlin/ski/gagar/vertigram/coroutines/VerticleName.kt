package ski.gagar.vertigram.coroutines

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

data class VerticleName(
    /**
     * User-defined coroutine name.
     */
    val name: String
) : AbstractCoroutineContextElement(VerticleName) {
    /**
     * Key for [VerticleName] instance in the coroutine context.
     */
    companion object Key : CoroutineContext.Key<VerticleName>

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String = "VerticleName($name)"
}
