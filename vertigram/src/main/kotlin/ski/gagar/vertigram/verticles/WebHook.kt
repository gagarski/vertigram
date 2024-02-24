package ski.gagar.vertigram.verticles

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import kotlinx.coroutines.delay
import ski.gagar.vertigram.client.Telegram
import ski.gagar.vertigram.client.TgVTelegram
import ski.gagar.vertigram.config.WebHookConfig
import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.jackson.publishJson
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.methods.deleteWebhook
import ski.gagar.vertigram.methods.setWebhook
import ski.gagar.vertigram.retrying
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER
import ski.gagar.vertigram.web.IpNetworkAddress
import ski.gagar.vertigram.web.RealIpLoggerHandler
import java.util.*

class WebHook : BaseVertigramVerticle() {
    private val secret = UUID.randomUUID()
    private val typedConfig by lazy {
        config.mapTo<Config>()
    }
    private val tg: Telegram by lazy {
        TgVTelegram(vertx, typedConfig.tgvAddress)
    }

    private lateinit var server: HttpServer

    override suspend fun start() {
        logger.lazy.info { "Deleting old webhook..." }
        retrying(coolDown = { delay(3000) }) {
            tg.deleteWebhook()
        }

        logger.lazy.info { "Staring $javaClass server..." }
        server = vertx.createHttpServer()
        val router = Router.router(vertx)
        router.route().handler(RealIpLoggerHandler(
            trustedNetworks = typedConfig.webHook.proxy?.trustedNetworks?.map { IpNetworkAddress(it) }?.toSet() ?: setOf(),
            trustDomainSockets = typedConfig.webHook.proxy?.trustDomainSockets ?: false))
        router.route().handler(BodyHandler.create())

        val addr =
            if (typedConfig.webHook.base.startsWith("/")) typedConfig.webHook.base else "/${typedConfig.webHook.base}"

        router.post(addr).handler { context ->
            if (context.request().getHeader(X_TELEGRAM_BOT_API_SECRET_TOKEN) != secret.toString()) {
                context.response().statusCode = HttpResponseStatus.FORBIDDEN.code()
                context.response().end()
                return@handler
            }
            val json = context.body().asJsonObject()
            val req = try {
                json.mapTo(
                    Update.Parsed::class.java,
                    TELEGRAM_JSON_MAPPER
                )
            } catch (ex: Exception) {
                logger.lazy.error(throwable = ex) { "Malformed update from Telegram $json, skipping it" }
                // It's ugly to send successful response back to Telegram.
                // But otherwise (either when returning 40x or 50x codes) Telegram will retry these requests
                // First, it's unclear when will it give up (docs say "after a reasonable amount of attempts")
                // Second, returning 400 or 500 blocks other updates (at least from same chat) unless Telegram gives up,
                // or we return 200.
                context.response().end()
                return@handler
            }
            logger.lazy.trace { "Received update $req" }
            logger.lazy.trace { "Publishing $req" }
            vertx.eventBus().publishJson(typedConfig.updatePublishingAddress, req)
            context.response().end()
        }

        server.requestHandler(router)
        server.listen(typedConfig.webHook.port, typedConfig.webHook.host)

        logger.lazy.info { "Setting new Telegram webhook..." }
        retrying(coolDown = { delay(3000) }) {
            tg.setWebhook(
                url = typedConfig.webHook.publicUrl,
                allowedUpdates = typedConfig.allowedUpdates,
                secretToken = secret.toString()
            )
        }

        logger.lazy.info { "Web server is listening..." }
    }

    override suspend fun stop() {
        server.close()
    }

    data class Config(
        val tgvAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE,
        val updatePublishingAddress: String = VertigramAddresses.UPDATES,
        val webHook: WebHookConfig = WebHookConfig(),
        val allowedUpdates: List<Update.Type>? = null
    ) {
        companion object {
            const val DEFAULT_UPDATE_PUBLISHING_ADDRESS = "ski.gagar.vertigram.updates"
        }
    }

    companion object {
        private const val X_TELEGRAM_BOT_API_SECRET_TOKEN = "X-Telegram-Bot-Api-Secret-Token"
    }
}
