package ski.gagar.vertigram.telegram.types.formattedtext

import ski.gagar.vertigram.telegram.types.MessageEntity

/**
 * Interface for input types (sent TO Telegram API) that has formatted text field
 */
interface HasFormattedText {
    val text: String
    val parseMode: FormattedText.ParseMode?
    val entities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has optional formatted text field
 */
interface HasOptionalFormattedText {
    val text: String?
    val textParseMode: FormattedText.ParseMode?
    val textEntities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has formatted text field
 */
interface HasFormattedQuestion {
    val question: String
    val questionParseMode: FormattedText.ParseMode?
    val questionEntities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has optional formatted caption field
 */
interface HasOptionalFormattedCaption {
    val caption: String?
    val parseMode: FormattedText.ParseMode?
    val captionEntities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has optional formatted explanation field
 */
interface HasOptionalFormattedExplanation {
    val explanation: String?
    val explanationParseMode: FormattedText.ParseMode?
    val explanationEntities: List<MessageEntity>?
}

/**
 * Interface for input types (sent TO Telegram API) that has optional formatted quote field
 */
interface HasOptionalFormattedQuote {
    val quote: String?
    val quoteParseMode: FormattedText.ParseMode?
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
 * Interface for output types (received FROM Telegram API) that has text with entities field
 */
interface HasTextWithEntities {
    val text: String
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
 * Interface for output types (received FROM Telegram API) that has question with entities field
 */
interface HasQuestionWithEntities {
    val question: String
    val questionEntities: List<MessageEntity>?
}


/**
 * Get text [FormattedText] for types that [HasFormattedText]
 */
val HasFormattedText.formattedText: FormattedText
    get() = FormattedText(text = text, parseMode = parseMode, entities = entities)

/**
 * Get text [FormattedText] for types that [HasFormattedText]
 */
val HasFormattedQuestion.formattedQuestion: FormattedText
    get() = FormattedText(text = question, parseMode = questionParseMode, entities = questionEntities)

/**
 * Get caption as [FormattedText] for types that [HasOptionalFormattedCaption]
 */
val HasOptionalFormattedCaption.formattedCaption: FormattedText?
    get() = caption?.let {
        FormattedText(text = it, parseMode = parseMode, entities = captionEntities)
    }

/**
 * Get explanation as [FormattedText] for types that [HasOptionalFormattedExplanation]
 */
val HasOptionalFormattedExplanation.formattedExplanation: FormattedText?
    get() = explanation?.let {
        FormattedText(text = it, parseMode = explanationParseMode, entities = explanationEntities)
    }

/**
 * Get explanation as [FormattedText] for types that [HasOptionalFormattedQuote]
 */
val HasOptionalFormattedQuote.formattedQuote: FormattedText?
    get() = quote?.let {
        FormattedText(text = it, parseMode = quoteParseMode, entities = quoteEntities)
    }

/**
 * Get text as [TextWithEntities] for types that [HasOptionalTextWithEntities]
 */
val HasOptionalTextWithEntities.textWithEntities: TextWithEntities?
    get() = text?.let { TextWithEntities(text = it, entities = entities ?: listOf()) }

/**
 * Get text as [TextWithEntities] for types that [HasOptionalTextWithEntities]
 */
val HasTextWithEntities.textWithEntities: TextWithEntities
    get() = text.let { TextWithEntities(text = it, entities = entities ?: listOf()) }

/**
 * Get caption as [TextWithEntities] for types that [HasOptionalCaptionWithEntities]
 */
val HasOptionalCaptionWithEntities.captionWithEntities: TextWithEntities?
    get() = caption?.let { TextWithEntities(text = it, entities = captionEntities ?: listOf()) }

/**
 * Get explanation as [TextWithEntities] for types that [HasOptionalExplanationWithEntities]
 */
val HasOptionalExplanationWithEntities.explanationWithEntities: TextWithEntities?
    get() = explanation?.let { TextWithEntities(text = it, entities = explanationEntities ?: listOf()) }

/**
 * Get question as [TextWithEntities] for types that [HasQuestionWithEntities]
 */
val HasQuestionWithEntities.questionWithEntities: TextWithEntities?
    get() = question.let { TextWithEntities(text = it, entities = questionEntities ?: listOf()) }
