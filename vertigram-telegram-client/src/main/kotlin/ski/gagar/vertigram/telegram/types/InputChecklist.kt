package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [InputChecklist](https://core.telegram.org/bots/api#inputchecklist) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class InputChecklist internal constructor(
    val title: String,
    val parseMode: RichText.ParseMode? = null,
    val titleEntities: List<MessageEntity>? = null,
    val tasks: List<Task>,
    val otherCanAddTasks: Boolean = false,
    val otherCanMarkTasksAsDone: Boolean = false,
) {
    /**
     * Telegram [ChecklistTask](https://core.telegram.org/bots/api#checklisttask) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
//    @TelegramCodegen.Type
    data class Task internal constructor(
        val id: Int,
        val text: String,
        val parseMode: RichText.ParseMode? = null,
        val textEntities: List<MessageEntity>? = null
    ) {
        companion object
    }
    companion object
}

fun InputChecklist.Task.Companion.create(
    noPosArgs: NoPosArgs = ski.gagar.vertigram.util.NoPosArgs.INSTANCE,
    id: Int,
    richText: RichText,
    textEntities: List<MessageEntity>? = null,
): InputChecklist.Task = InputChecklist.Task(id = id, text = richText.text, parseMode = richText.parseMode, textEntities = textEntities)

fun InputChecklist.Task.Companion.invoke(
    noPosArgs: NoPosArgs = ski.gagar.vertigram.util.NoPosArgs.INSTANCE,
    id: Int,
    richText: RichText,
    textEntities: List<MessageEntity>? = null,
): InputChecklist.Task = InputChecklist.Task(id = id, text = richText.text, parseMode = richText.parseMode, textEntities = textEntities)
