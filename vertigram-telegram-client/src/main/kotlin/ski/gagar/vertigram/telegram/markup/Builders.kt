package ski.gagar.vertigram.telegram.markup

import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.richtext.HtmlText
import ski.gagar.vertigram.telegram.types.richtext.MarkdownV2Text
import ski.gagar.vertigram.telegram.types.richtext.TextWithEntities
import ski.gagar.vertigram.util.NoPosArgs

/**
 * A builder for [ReplyMarkup.InlineKeyboard].
 *
 * @sample ski.gagar.vertigram.samples.inlineKeyboardSample
 */
fun inlineKeyboard(init: InlineKeyboardMarkupBuilder.() -> Unit): ReplyMarkup.InlineKeyboard {
    val bld = InlineKeyboardMarkupBuilder()
    bld.init()
    return bld.build()
}

/**
 * A builder for [ReplyMarkup.Keyboard].
 *
 * Payloads for button types are unwrapped.
 *
 * @sample ski.gagar.vertigram.samples.keyboardSample
 */
fun keyboard(
    @Suppress("UNUSED_PARAMETER")
    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    isPersistent: Boolean = false,
    resizeKeyboard: Boolean = false,
    oneTimeKeyboard: Boolean = false,
    inputFieldPlaceholder: String? = null,
    selective: Boolean = false,
    init: ReplyKeyboardMarkupBuilder.() -> Unit
): ReplyMarkup.Keyboard {
    val bld = ReplyKeyboardMarkupBuilder()
    bld.init()
    return bld.build(
        isPersistent = isPersistent,
        resizeKeyboard = resizeKeyboard,
        oneTimeKeyboard = oneTimeKeyboard,
        inputFieldPlaceholder = inputFieldPlaceholder,
        selective = selective
    )
}

/**
 * A builder for [ReplyMarkup.KeyboardRemove]
 */
fun keyboardRemove(selective: Boolean) = ReplyMarkup.KeyboardRemove(selective = selective)

/**
 * A builder for [ReplyMarkup.ForceReply]
 */
fun forceReply(inputFieldPlaceholder: String? = null,
               selective: Boolean) =
    ReplyMarkup.ForceReply(
        inputFieldPlaceholder = inputFieldPlaceholder,
        selective = selective
    )

/**
 * A builder to convert plain text to [ski.gagar.vertigram.telegram.types.richtext.RichText].
 *
 * The text is treated as if it has no special mark up.
 */
fun String.toRichText() = TextWithEntities(this)

/**
 * Build [MarkdownV2Text] from [init].
 */
fun textMarkdown(init: RichTextRoot.() -> Unit) =
    MarkdownV2Text(RichTextRoot().apply(init).toMarkdownString())

/**
 * Build [HtmlText] from [init].
 */
fun textHtml(init: RichTextRoot.() -> Unit) =
    HtmlText(RichTextRoot().apply(init).toHtmlString())

/**
 * Build [TextWithEntities] from [init].
 */
fun textWithEntities(init: RichTextRoot.() -> Unit) =
    RichTextRoot().apply(init).toTextWithEntities()