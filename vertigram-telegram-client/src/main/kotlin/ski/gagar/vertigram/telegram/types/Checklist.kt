package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Telegram [Checklist](https://core.telegram.org/bots/api#checklist) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type(wrapRichText = false)
data class Checklist internal constructor(
    val title: String,
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
    @TelegramCodegen.Type(wrapRichText = false)
    data class Task internal constructor(
        val id: Int,
        val text: String,
        val textEntities: List<MessageEntity>? = null,
        val completedByUser: User? = null,
        val completionDate: Instant? = null
    ) {
        companion object
    }
    companion object
}
