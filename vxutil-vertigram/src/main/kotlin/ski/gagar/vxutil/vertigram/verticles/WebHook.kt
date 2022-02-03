package ski.gagar.vxutil.vertigram.verticles

import com.fasterxml.jackson.databind.JsonMappingException
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import ski.gagar.vxutil.ErrorLoggingCoroutineVerticle
import ski.gagar.vxutil.jackson.mapTo
import ski.gagar.vxutil.jackson.publishJson
import ski.gagar.vxutil.logger
import ski.gagar.vxutil.retrying
import ski.gagar.vxutil.sleep
import ski.gagar.vxutil.vertigram.client.Telegram
import ski.gagar.vxutil.vertigram.client.TgVTelegram
import ski.gagar.vxutil.vertigram.config.WebHookConfig
import ski.gagar.vxutil.vertigram.entities.ParsedUpdate
import ski.gagar.vxutil.vertigram.entities.ParsedUpdateList
import ski.gagar.vxutil.vertigram.entities.requests.DeleteWebhook
import ski.gagar.vxutil.vertigram.entities.requests.SetWebhook
import ski.gagar.vxutil.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.web.IpNetworkAddress
import ski.gagar.vxutil.web.RealIpLoggerHandler
import java.util.*

class WebHook : ErrorLoggingCoroutineVerticle() {
    private val secret = UUID.randomUUID()
    private val typedConfig by lazy {
        config.mapTo<Config>()
    }
    private val tg: Telegram by lazy {
        TgVTelegram(vertx, typedConfig.tgvAddress)
    }

    override suspend fun start() {
        logger.info("Deleting old webhook...")
        retrying(coolDown = { sleep(3000) }) {
            tg.call(DeleteWebhook)
        }

        logger.info("Staring ski.gagar.vxutil.web server...")
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        router.route().handler(RealIpLoggerHandler(
            trustedNetworks = typedConfig.webHook.proxy?.trustedNetworks?.map { IpNetworkAddress(it) }?.toSet() ?: setOf(),
            trustDomainSockets = typedConfig.webHook.proxy?.trustDomainSockets ?: false))
        router.route().handler(BodyHandler.create())

        router.post("${typedConfig.webHook.base}/${secret}").handler { context ->
            logger.error("${context.bodyAsJson}")
            val json = context.bodyAsJson
            val req = try {
                json.mapTo(
                    ParsedUpdate::class.java,
                    TELEGRAM_JSON_MAPPER
                )
            } catch (ex: JsonMappingException) {
                logger.error("Malformed update from Telegram $json, skipping it", ex)
                // It's ugly to send successful response back to Telegram.
                // But otherwise (either when returning 40x or 50x codes) Telegram will retry these requests
                // First, it's unclear when will it give up (docs say "after a reasonable amount of attempts")
                // Second, returning 400 or 500 blocks other updates (at least from same chat) unless Telegram gives up,
                // or we return 200.
                context.response().end()
                return@handler
            }
            logger.trace("Received update $req")
            logger.trace("Publishing $req")
            vertx.eventBus().publishJson(typedConfig.updatePublishingAddress, ParsedUpdateList(listOf(req)))
            context.response().end()
        }

        server.requestHandler(router)
        server.listen(typedConfig.webHook.port, typedConfig.webHook.host)

        logger.info("Setting new Telegram webhook...")
        retrying(coolDown = { sleep(3000) }) {
            tg.call(SetWebhook("${typedConfig.webHook.publicUrl}/${secret}"))
        }

        logger.info("Web server is listening...")
    }

    data class Config(
        val tgvAddress: String = TelegramVerticle.Config.DEFAULT_BASE_ADDRESS,
        val updatePublishingAddress: String = DEFAULT_UPDATE_PUBLISHING_ADDRESS,
        val webHook: WebHookConfig = WebHookConfig()
    ) {
        companion object {
            const val DEFAULT_UPDATE_PUBLISHING_ADDRESS = "ski.gagar.vertigram.updates"
        }
    }
}
