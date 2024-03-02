package ski.gagar.vertigram.services

import ski.gagar.vertigram.Vertigram
import java.util.*

/**
 * A provider for [Vertigram] initialization.
 *
 * Each instance auto-discovered by [ServiceLoader] will be called ([initialize] method) on [Vertigram] initialization,
 * unless the list if [VertigramInitializer]s is explicitly provided.
 */
interface VertigramInitializer {
    fun Vertigram.initialize()
}