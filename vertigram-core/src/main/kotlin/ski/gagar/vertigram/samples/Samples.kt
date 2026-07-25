package ski.gagar.vertigram.samples

import ski.gagar.vertigram.verticles.common.VertigramVerticle

private fun automaticConfigTypeExample() {
    data class Config(val something: String)

    class ExampleVerticle : VertigramVerticle<Config>()
}
