package ski.gagar.vertigram.verticles.telegram

import ski.gagar.vertigram.verticles.common.HierarchyVerticle
import ski.gagar.vertigram.verticles.common.address.VertigramCommonAddress
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

abstract class TelegramDialogVerticle<Config> : HierarchyVerticle<Config>() {
    /**
     * Callback query listen address. By default — a private address, meaning that only parent verticle knows it.
     *
     * May be overridden by subclasses
     *
     * @see HierarchyVerticle
     */
    open val callbackQueryListenAddress: String?
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, TelegramAddress.Dialog.Classifier.CallbackQuery)

    /**
     * Callback query listen address. By default — a private address, meaning that only parent verticle knows it.
     *
     * May be overridden by subclasses
     *
     * @see HierarchyVerticle
     */
    open val messageListenAddress: String?
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, TelegramAddress.Dialog.Classifier.Message)
}