package ski.gagar.vxutil.vertigram.tools

import ski.gagar.vxutil.vertigram.types.Me
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntityType
import ski.gagar.vxutil.vertigram.util.InstantiatedEntity
import ski.gagar.vxutil.vertigram.util.withText

private fun InstantiatedEntity.isCommandForBot(command: String, bot: Me?) =
    type == MessageEntityType.BOT_COMMAND && (text == "/$command" || (bot != null && text == "/$command@${bot.username}"))


fun Message.getCommandForBot(command: String, bot: Me?) =
    entities.withText(text).firstOrNull {
        it.type == MessageEntityType.BOT_COMMAND && it.offset == 0 && it.isCommandForBot(command, bot)
    }

fun Message.isCommandForBot(command: String, bot: Me?) =
    getCommandForBot(command, bot) != null