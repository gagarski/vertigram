package ski.gagar.vxutil.verticles

interface Named {
    val name: String

    companion object {
        const val VERTICLE_NAME_MDC = "verticleName"
    }
}
