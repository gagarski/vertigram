package ski.gagar.vertigram.verticles

import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import ski.gagar.vxutil.logger
import ski.gagar.vertigram.client.Telegram
import ski.gagar.vertigram.client.TgVTelegram
import ski.gagar.vertigram.config.WebHookConfig
import ski.gagar.vertigram.entities.Update
import ski.gagar.vertigram.entities.requests.DeleteWebhook
import ski.gagar.vertigram.entities.requests.SetWebhook
import ski.gagar.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vertigram.messages.UpdateList
import ski.gagar.vxutil.ip.IpNetworkAddress
import ski.gagar.vxutil.mapTo
import ski.gagar.vxutil.publishJson
import ski.gagar.vxutil.retrying
import ski.gagar.vxutil.sleep
import ski.gagar.vxutil.web.RealIpFormatter
import java.util.*

class WebHook : CoroutineVerticle() {
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

        logger.info("Staring web server...")
        val server = vertx.createHttpServer()
        val router = Router.router(vertx)
        router.route().handler(LoggerHandler.create(LoggerFormat.CUSTOM).customFormatter(
            RealIpFormatter(
                trustedNetworks = typedConfig.webHook.proxy?.trustedNetworks?.map { IpNetworkAddress(it) }?.toSet() ?: setOf(),
                trustDomainSockets = typedConfig.webHook.proxy?.trustDomainSockets ?: false)))
        router.route().handler(BodyHandler.create())

        router.post("${typedConfig.webHook.base}/${secret}").handler { context ->
            val req = context.bodyAsJson.mapTo(Update::class.java,
                TELEGRAM_JSON_MAPPER
            )
            logger.trace("Received update $req")
            logger.trace("Publishing $req")
            vertx.eventBus().publishJson(typedConfig.updatePublishingAddress, UpdateList(req))
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
