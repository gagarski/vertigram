package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText

/**
 * Describes a checklist to create.
 *
 * See Telegram's [InputChecklist](https://core.telegram.org/bots/api#inputchecklist) documentation.
 */
@TelegramCodegen.Type
data class InputChecklist internal constructor(
    /** Title of the checklist; 1-255 characters after entities parsing. */
    val title: String,
    /** Mode for parsing entities in the checklist title. See Telegram's formatting options for details. */
    val parseMode: FormattedText.ParseMode? = null,
    /**
     * List of special entities that appear in the checklist title, which can be specified instead of a parsing mode.
     * Currently, only bold, italic, underline, strikethrough, spoiler, custom emoji, and date-time entities are allowed.
     */
    val titleEntities: List<MessageEntity>? = null,
    /** List of 1-30 tasks in the checklist. */
    val tasks: List<Task>,
    /** `true` if other users can add tasks to the checklist. */
    val otherCanAddTasks: Boolean = false,
    /** `true` if other users can mark tasks as done or not done in the checklist. */
    val otherCanMarkTasksAsDone: Boolean = false,
) {
    /**
     * Describes a task to add to a checklist.
     *
     * See Telegram's [InputChecklistTask](https://core.telegram.org/bots/api#inputchecklisttask) documentation.
     */
    @TelegramCodegen.Type
    data class Task internal constructor(
        /** Unique identifier of the task; must be positive and unique among all current task identifiers. */
        val id: Int,
        /** Text of the task; 1-100 characters after entities parsing. */
        val text: String,
        /** Mode for parsing entities in the task text. See Telegram's formatting options for details. */
        val parseMode: FormattedText.ParseMode? = null,
        /**
         * List of special entities that appear in the task text, which can be specified instead of a parsing mode.
         * Currently, only bold, italic, underline, strikethrough, spoiler, custom emoji, and date-time entities are
         * allowed.
         */
        val textEntities: List<MessageEntity>? = null
    ) {
        companion object
    }
    companion object
}
