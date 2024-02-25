package ski.gagar.vertigram.verticles

/**
 * An entity with name
 */
interface Named {
    val name: String

    companion object {
        const val VERTICLE_NAME_MDC = "verticleName"
    }
}
