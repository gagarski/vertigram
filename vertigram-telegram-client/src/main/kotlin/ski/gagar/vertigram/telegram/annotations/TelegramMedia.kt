package ski.gagar.vertigram.telegram.annotations

/**
 * An annotation to mark media fields in telegram multipart methods.
 *
 * [ski.gagar.vertigram.telegram.types.attachments.Attachment] and
 * [ski.gagar.vertigram.telegram.types.InputMedia] (single or list) can be marked.
 *
 * The annotation is used by [ski.gagar.vertigram.util.multipart.ObjectMapperWithMultipart] to detect which fields
 * contain attachment description
 */
internal annotation class TelegramMedia
