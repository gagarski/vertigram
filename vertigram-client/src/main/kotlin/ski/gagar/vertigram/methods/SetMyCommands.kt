package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.BotCommand
import ski.gagar.vertigram.types.BotCommandScope
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMyCommands](https://core.telegram.org/bots/api#setmycommands) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetMyCommands(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val commands: List<BotCommand>,
    val scope: BotCommandScope? = null,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
