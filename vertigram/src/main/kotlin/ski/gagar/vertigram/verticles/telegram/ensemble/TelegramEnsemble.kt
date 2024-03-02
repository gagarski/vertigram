package ski.gagar.vertigram.verticles.telegram.ensemble

import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.telegram.client.DirectTelegram
import ski.gagar.vertigram.telegram.throttling.ThrottlingOptions
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.verticles.common.address.VertigramCommonAddress
import ski.gagar.vertigram.verticles.telegram.LongPoller
import ski.gagar.vertigram.verticles.telegram.TelegramVerticle
import ski.gagar.vertigram.verticles.telegram.UpdateDispatcher
import ski.gagar.vertigram.verticles.telegram.WebHook
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress
import ski.gagar.vertigram.verticles.telegram.config.LongPollerConfig
import ski.gagar.vertigram.verticles.telegram.config.UpdateReceiverConfig
import ski.gagar.vertigram.verticles.telegram.config.WebHookConfig
import java.util.*

private const val PRIVATE_UPDATES_CLASSIFIER = "updates"

/**
 * Deployment descriptor returned from [deployTelegramEnsemble].
 *
 * Allows you to undeploy all ensemble verticles.
 */
data class TelegramEnsembleDeploymentDescriptor(
    private val vertigram: Vertigram,
    private val telegramId: String,
    private val updateReceiverId: String,
    private val updateDispatcherId: String
) {
    suspend fun undeploy() {
        vertigram.undeploy(updateDispatcherId)
        vertigram.undeploy(updateReceiverId)
        vertigram.undeploy(telegramId)
    }
}

/**
 * Deploy verticles to interact with Telegram:
 *  - [TelegramVerticle]
 *  - [LongPoller] or [WebHook]
 *  - [UpdateDispatcher]
 */
suspend fun Vertigram.deployTelegramEnsemble(
    /**
     * Telegram Bot API token
     */
    token: String,
    /**
     * Allowed updates to pass to `setWebHook` or `getUpades`
     */
    allowedUpdates: List<Update.Type>,
    /**
     * Base address for [TelegramVerticle]
     */
    telegramAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE,
    /**
     * Options for [DirectTelegram] wrapped in [TelegramVerticle]
     */
    telegramOptions: DirectTelegram.Options = DirectTelegram.Options(),
    /**
     * Throttling options for [TelegramVerticle]
     */
    throttling: ThrottlingOptions = ThrottlingOptions(),
    /**
     * Address to publish updates from [LongPoller] or [WebHook].
     *
     * `null` means that updates will be published to a private address, known only by [UpdateDispatcher]
     */
    updatePublishingAddress: String? = null,
    /**
     * Should [LongPoller]/[WebHook] skip with the date before they were deployed.
     */
    skipMissed: Boolean = true,
    /**
     * Config for update receiver: [LongPoller] or [WebHook]
     */
    updateReceiverConfig: UpdateReceiverConfig = LongPollerConfig,
    /**
     * Address for publishing updates dispatched and unwrapped by [UpdateDispatcher]
     */
    updateDispatchAddressBase: String = TelegramAddress.DEMUX_BASE
) : TelegramEnsembleDeploymentDescriptor {
    val telegramId = deployVerticle(
        TelegramVerticle(),
        TelegramVerticle.Config(
            token = token,
            baseAddress = telegramAddress,
            telegramOptions = telegramOptions,
            throttling = throttling
        )
    )

    val updateAddress = updatePublishingAddress
        ?: VertigramCommonAddress.Private.withClassifier("${UUID.randomUUID()}", PRIVATE_UPDATES_CLASSIFIER)

    val updateReceiverId = when (updateReceiverConfig) {
        is LongPollerConfig -> {
            deployVerticle(LongPoller(), LongPoller.Config(
                allowedUpdates = allowedUpdates,
                telegramAddress = telegramAddress,
                updatePublishingAddress = updateAddress,
                skipMissed = skipMissed
            ))
        }
        is WebHookConfig -> {
            deployVerticle(WebHook(), WebHook.Config(
                allowedUpdates = allowedUpdates,
                telegramAddress = telegramAddress,
                updatePublishingAddress = updateAddress,
                skipMissed = skipMissed,
                webHook = updateReceiverConfig
            ))
        }
    }

    val updateDispatcherId = deployVerticle(
        UpdateDispatcher(),
        UpdateDispatcher.Config(
            listen = updateAddress,
            publishBase = updateDispatchAddressBase
        )
    )

    return TelegramEnsembleDeploymentDescriptor(
        vertigram = this,
        telegramId = telegramId,
        updateReceiverId = updateReceiverId,
        updateDispatcherId = updateDispatcherId
    )
}