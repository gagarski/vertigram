package ski.gagar.vertigram.verticles.telegram.config

import com.fasterxml.jackson.annotation.JsonIgnore

data object LongPollerConfig : UpdateReceiverConfig {
    @JsonIgnore
    override val type: UpdateReceiverConfig.Type = UpdateReceiverConfig.Type.LONG_POLL
}
