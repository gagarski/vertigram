package ski.gagar.vertigram.verticles

import io.vertx.core.Context
import io.vertx.core.Vertx
import ski.gagar.vertigram.BOOTSTRAP_JSON_MAPPER
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.getVertigram
import ski.gagar.vertigram.jackson.mapTo

abstract class SimpleVertigramVerticle : BaseVertigramVerticle() {
    lateinit var vertigram: Vertigram
        private set

    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        println(this.config)
        val bareBonesConfig = config.mapTo<BareBonesConfig>(BOOTSTRAP_JSON_MAPPER)
        vertigram = vertx.getVertigram(bareBonesConfig.vertigramName)
    }

    data class BareBonesConfig(
        val vertigramName: String
    )
}