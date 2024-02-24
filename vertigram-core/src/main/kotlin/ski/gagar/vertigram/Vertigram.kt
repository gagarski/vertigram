package ski.gagar.vertigram

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.core.shareddata.Shareable
import io.vertx.kotlin.coroutines.coAwait
import ski.gagar.vertigram.jackson.toJsonObject
import ski.gagar.vertigram.verticles.SimpleVertigramVerticle
import ski.gagar.vertigram.verticles.VertigramVerticle

const val VERTIGRAMS = "ski.gagar.vertigram.vertigrams"

fun defaultVertigramMapper() =
    DatabindCodec.mapper().copy()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())



class Vertigram(
    val vertx: Vertx,
    config: Config
) : Shareable {
    val name: String = config.name
    val objectMapper: ObjectMapper = config.objectMapper
    val eventBus: EventBus = EventBus()

    fun <T> deploymentOptions(config: T) = DeploymentOptions(this, config)

    suspend fun <T> deployVerticle(verticle: VertigramVerticle<T>, deploymentOptions: DeploymentOptions<T>) {
        vertx.deployVerticle(verticle, deploymentOptions).coAwait()
    }

    suspend fun deployVerticle(verticle: SimpleVertigramVerticle) {
        vertx.deployVerticle(verticle, DeploymentOptions(this)).coAwait()
    }

    inner class EventBus : io.vertx.core.eventbus.EventBus by vertx.eventBus() {
        val objectMapper: ObjectMapper
            get() = this@Vertigram.objectMapper
    }

    class Config(
        val name: String = DEFAULT_NAME,
        val objectMapper: ObjectMapper = defaultVertigramMapper()
    ) {
        companion object {
            const val DEFAULT_NAME = "default"
        }
    }

    class DeploymentOptions<Config>(
        vertigram: Vertigram,
        config: Config
    ): io.vertx.core.DeploymentOptions() {
        init {
            @Suppress("DEPRECATION")
            setConfig(VertigramVerticle.ConfigWrapper(vertigram.name, config).toJsonObject(vertigram.objectMapper))
        }

        @Deprecated(message = "Use constructor argument instead")
        override fun getConfig(): JsonObject {
            return super.getConfig()
        }

        @Deprecated(message = "Use constructor argument instead")
        override fun setConfig(config: JsonObject?): io.vertx.core.DeploymentOptions {
            return super.setConfig(config)
        }
    }

    fun DeploymentOptions(
        vertigram: Vertigram
    ) = DeploymentOptions(vertigram, null)
}

fun Vertx.attachVertigram(config: Vertigram.Config = Vertigram.Config()): Vertigram =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS)
        .compute(config.name) { _, old ->
            if (old != null)
                throw IllegalStateException("The data source with this name is already present")

            Vertigram(this, config)
        }!!

fun Vertx.detachVertigram(name: String = Vertigram.Config.DEFAULT_NAME) =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS).remove(name)

fun Vertx.getVertigram(name: String = Vertigram.Config.DEFAULT_NAME) =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS)[name]
        ?: throw IllegalArgumentException("Vertigram $name is not attached to this Ver.X instance")