package ski.gagar.vertigram.telegram.types.util

import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.User

private fun InstantiatedEntity.isCommandForBot(command: String, bot: User.Me?) =
    entity.type == MessageEntity.Type.BOT_COMMAND && (textPart == "/$command" || (bot != null && textPart == "/$command@${bot.username}"))


fun Message.getCommandForBot(command: String, bot: User.Me?) =
    text?.let { text ->
        entities.withText(text).firstOrNull {
            it.entity.type == MessageEntity.Type.BOT_COMMAND && it.entity.offset == 0 && it.isCommandForBot(command, bot)
        }
    }

fun Message.isCommandForBot(command: String, bot: User.Me?) =
    getCommandForBot(command, bot) != null
