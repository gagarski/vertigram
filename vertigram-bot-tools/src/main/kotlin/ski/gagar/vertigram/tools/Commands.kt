package ski.gagar.vertigram.tools

import ski.gagar.vertigram.tools.messages.InstantiatedEntity
import ski.gagar.vertigram.tools.messages.withText
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.User

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
