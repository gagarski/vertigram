package ski.gagar.vertigram.types.richtext

import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode

/**
 * Interface for input types (sent TO Telegram API) that has rich text field
 */
interface HasRichText {
    val text: String
    val parseMode: ParseMode?
    val entities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has optional rich caption field
 */
interface HasOptionalRichCaption {
    val caption: String?
    val parseMode: ParseMode?
    val captionEntities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has optional rich explanation field
 */
interface HasOptionalRichExplanation {
    val explanation: String?
    val explanationParseMode: ParseMode?
    val explanationEntities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has optional rich quote field
 */
interface HasOptionalRichQuote {
    val quote: String?
    val quoteParseMode: ParseMode?
    val quoteEntities: List<MessageEntity>?
}

/**
 * Interface for output types (received FROM Telegram API) that has optional text with entities field
 */
interface HasOptionalTextWithEntities {
    val text: String?
    val entities: List<MessageEntity>?
}

/**
 * Interface for output types (received FROM Telegram API) that has optional caption with entities field
 */
interface HasOptionalCaptionWithEntities {
    val caption: String?
    val captionEntities: List<MessageEntity>?
}

/**
 * Interface for output types (received FROM Telegram API) that has optional explanation with entities field
 */
interface HasOptionalExplanationWithEntities {
    val explanation: String?
    val explanationEntities: List<MessageEntity>?
}


/**
 * Get text [RichText] for types that [HasRichText]
 */
val HasRichText.richText: RichText
    get() = RichText(text = text, parseMode = parseMode, entities = entities)

/**
 * Get caption as [RichText] for types that [HasOptionalRichCaption]
 */
val HasOptionalRichCaption.richCaption: RichText?
    get() = caption?.let {
        RichText(text = it, parseMode = parseMode, entities = captionEntities)
    }

/**
 * Get explanation as [RichText] for types that [HasOptionalRichExplanation]
 */
val HasOptionalRichExplanation.richExplanation: RichText?
    get() = explanation?.let {
        RichText(text = it, parseMode = explanationParseMode, entities = explanationEntities)
    }

/**
 * Get explanation as [RichText] for types that [HasOptionalRichQuote]
 */
val HasOptionalRichQuote.richQuote: RichText?
    get() = quote?.let {
        RichText(text = it, parseMode = quoteParseMode, entities = quoteEntities)
    }

/**
 * Get text as [TextWithEntities] for types that [HasOptionalTextWithEntities]
 */
val HasOptionalTextWithEntities.textWithEntities: TextWithEntities?
    get() = text?.let { TextWithEntities(text = it, entities = entities ?: listOf()) }

/**
 * Get text as [TextWithEntities] for types that [HasOptionalCaptionWithEntities]
 */
val HasOptionalCaptionWithEntities.captionWithEntities: TextWithEntities?
    get() = caption?.let { TextWithEntities(text = it, entities = captionEntities ?: listOf()) }

/**
 * Get text as [TextWithEntities] for types that [HasOptionalExplanationWithEntities]
 */
val HasOptionalExplanationWithEntities.explanationWithEntities: TextWithEntities?
    get() = explanation?.let { TextWithEntities(text = it, entities = explanationEntities ?: listOf()) }