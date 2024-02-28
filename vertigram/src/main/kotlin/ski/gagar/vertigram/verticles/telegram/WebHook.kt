package ski.gagar.vertigram.verticles.telegram

import com.fasterxml.jackson.core.type.TypeReference
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import kotlinx.coroutines.delay
import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.retrying
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.methods.deleteWebhook
import ski.gagar.vertigram.telegram.methods.setWebhook
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress
import ski.gagar.vertigram.verticles.telegram.config.WebHookConfig
import ski.gagar.vertigram.web.server.IpNetworkAddress
import ski.gagar.vertigram.web.server.RealIpLoggerHandler
import java.time.Instant
import java.util.*

class WebHook : UpdateReceiver<WebHook.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()
    private val secret = UUID.randomUUID()
    private val tg: Telegram by lazy {
        ThinTelegram(vertigram, typedConfig.telegramAddress)
    }

    private lateinit var server: HttpServer

    override suspend fun start() {
        val startDate = Instant.now()
        logger.lazy.info { "Deleting old webhook..." }
        retrying(coolDown = { delay(3000) }) {
            tg.deleteWebhook()
        }

        logger.lazy.info { "Staring $javaClass server..." }
        server = vertx.createHttpServer()
        val router = Router.router(vertx)
        router.route().handler(
            RealIpLoggerHandler(
            trustedNetworks = typedConfig.webHook.proxy?.trustedNetworks?.map { IpNetworkAddress(it) }?.toSet() ?: setOf(),
            trustDomainSockets = typedConfig.webHook.proxy?.trustDomainSockets ?: false)
        )
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
            val date = req.date
            if (!typedConfig.skipMissed || date == null || date >= startDate) {
                vertigram.eventBus.publish(typedConfig.updatePublishingAddress, req)
            }
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
        override val telegramAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE,
        override val allowedUpdates: List<Update.Type>,
        override val updatePublishingAddress: String = TelegramAddress.UPDATES,
        override val skipMissed: Boolean = true,
        val webHook: WebHookConfig = WebHookConfig()
    ) : UpdateReceiver.Config {
        companion object {
            const val DEFAULT_UPDATE_PUBLISHING_ADDRESS = "ski.gagar.vertigram.updates"
        }
    }

    companion object {
        private const val X_TELEGRAM_BOT_API_SECRET_TOKEN = "X-Telegram-Bot-Api-Secret-Token"
    }
}
