package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.BotCommand
import ski.gagar.vertigram.types.BotCommandScope
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyCommands](https://core.telegram.org/bots/api#getmycommands) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetMyCommands(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val scope: BotCommandScope? = null,
    val languageCode: String? = null
) : JsonTelegramCallable<List<BotCommand>>()