package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Describes a checklist.
 *
 * See Telegram's [Checklist](https://core.telegram.org/bots/api#checklist) documentation.
 */
@TelegramCodegen.Type
data class Checklist internal constructor(
    /** Title of the checklist. */
    val title: String,
    /** Special entities that appear in the checklist title. */
    val titleEntities: List<MessageEntity>? = null,
    /** List of tasks in the checklist. */
    val tasks: List<Task>,
    /** `true` if users other than the creator of the list can add tasks to the list. */
    val otherCanAddTasks: Boolean = false,
    /** `true` if users other than the creator of the list can mark tasks as done or not done. */
    val otherCanMarkTasksAsDone: Boolean = false,
) {
    /**
     * Describes a task in a checklist.
     *
     * See Telegram's [ChecklistTask](https://core.telegram.org/bots/api#checklisttask) documentation.
     */
    @TelegramCodegen.Type
    data class Task internal constructor(
        /** Unique identifier of the task. */
        val id: Int,
        /** Text of the task. */
        val text: String,
        /** Special entities that appear in the task text. */
        val textEntities: List<MessageEntity>? = null,
        /** User that completed the task; omitted if the task wasn't completed by a user. */
        val completedByUser: User? = null,
        /** Chat that completed the task; omitted if the task wasn't completed by a chat. */
        val completedByChat: Chat? = null,
        /** Point in time when the task was completed; the Unix epoch if the task wasn't completed. */
        val completionDate: Instant? = null
    ) {
        companion object
    }
    companion object
}
