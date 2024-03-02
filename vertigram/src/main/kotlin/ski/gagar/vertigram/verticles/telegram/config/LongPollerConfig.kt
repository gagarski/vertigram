package ski.gagar.vertigram.verticles.telegram.config

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Config to enable long-polling in [ski.gagar.vertigram.verticles.telegram.ensemble.deployTelegramEnsemble]
 */
data object LongPollerConfig : UpdateReceiverConfig {
    @JsonIgnore
    override val type: UpdateReceiverConfig.Type = UpdateReceiverConfig.Type.LONG_POLL
}
