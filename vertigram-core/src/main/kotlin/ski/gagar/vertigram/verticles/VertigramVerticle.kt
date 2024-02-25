package ski.gagar.vertigram.verticles

import com.fasterxml.jackson.core.type.TypeReference
import io.vertx.core.Context
import io.vertx.core.Vertx
import ski.gagar.vertigram.jackson.mapTo


abstract class VertigramVerticle<Config> : BaseVertigramVerticle() {
    protected abstract val typeReference: TypeReference<Config>

    private lateinit var configHolder: ConfigHolder<Config>

    protected val typedConfig: Config
        get() = configHolder.config


    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        val theConfig = config.getJsonObject("config")
        configHolder = ConfigHolder(theConfig.mapTo(typeReference, vertigram.objectMapper))
    }


    data class ConfigWrapper<Config>(
        val vertigramName: String,
        val config: Config
    )

    private data class ConfigHolder<T>(val config: T)

}