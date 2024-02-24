package ski.gagar.vertigram.verticles

import com.fasterxml.jackson.core.type.TypeReference
import io.vertx.core.Context
import io.vertx.core.Vertx
import ski.gagar.vertigram.BOOTSTRAP_JSON_MAPPER
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.getVertigram
import ski.gagar.vertigram.jackson.mapTo


abstract class VertigramVerticle<Config> : BaseVertigramVerticle() {
    protected abstract val typeReference: TypeReference<Config>

    lateinit var vertigram: Vertigram
        private set
    private lateinit var configHolder: ConfigHolder<Config>

    protected val typedConfig: Config
        get() = configHolder.config


    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        println(this.config)
        val bareBonesConfig = config.mapTo<BareBonesConfig>(BOOTSTRAP_JSON_MAPPER)
        println(bareBonesConfig)
        vertigram = vertx.getVertigram(bareBonesConfig.vertigramName)
        val theConfig = config.getJsonObject("config")
        configHolder = ConfigHolder(theConfig.mapTo(typeReference, vertigram.objectMapper))
    }


    data class ConfigWrapper<Config>(
        val vertigramName: String,
        val config: Config
    )

    data class BareBonesConfig(
        val vertigramName: String
    )

    private data class ConfigHolder<T>(val config: T)

}