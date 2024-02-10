package ski.gagar.vertigram.tools

import ski.gagar.vertigram.types.Me
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntityType
import ski.gagar.vertigram.tools.messages.InstantiatedEntity
import ski.gagar.vertigram.tools.messages.withText

private fun InstantiatedEntity.isCommandForBot(command: String, bot: Me?) =
    type == MessageEntityType.BOT_COMMAND && (text == "/$command" || (bot != null && text == "/$command@${bot.username}"))


fun Message.getCommandForBot(command: String, bot: Me?) =
    entities.withText(text).firstOrNull {
        it.type == MessageEntityType.BOT_COMMAND && it.offset == 0 && it.isCommandForBot(command, bot)
    }

fun Message.isCommandForBot(command: String, bot: Me?) =
    getCommandForBot(command, bot) != null
