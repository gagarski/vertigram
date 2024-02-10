package ski.gagar.vertigram.markup

import kotlinx.html.FlowContent
import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.HTMLTag
import kotlinx.html.HtmlBlockInlineTag
import kotlinx.html.TagConsumer
import kotlinx.html.attributesMapOf
import kotlinx.html.b
import kotlinx.html.blockQuote
import kotlinx.html.body
import kotlinx.html.code
import kotlinx.html.pre
import kotlinx.html.span
import kotlinx.html.stream.appendHTML
import kotlinx.html.visit
import ski.gagar.vertigram.richtext.CaptionWithEntities
import ski.gagar.vertigram.richtext.ExplanationWithEntities
import ski.gagar.vertigram.richtext.MarkdownCaption
import ski.gagar.vertigram.richtext.MarkdownExplanation
import ski.gagar.vertigram.richtext.MarkdownQuote
import ski.gagar.vertigram.richtext.MarkdownText
import ski.gagar.vertigram.richtext.QuoteWithEntities
import ski.gagar.vertigram.richtext.TextWithEntities
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.MessageEntityType
import ski.gagar.vertigram.types.User

//////////////
// For Removal
//////////////
@JvmInline
value class Markdown internal constructor(private val rendered: String) {
    override fun toString() = rendered
}

fun md(init: RichTextRoot.() -> Unit) =
    Markdown(RichTextRoot().apply(init).toMarkdownString())

fun String.toMarkdown() = md { +this@toMarkdown }

////////////
// /For Removal
////////////

fun String.toRichText() = TextWithEntities(this)

fun textMarkdown(init: RichTextRoot.() -> Unit) =
    MarkdownText(RichTextRoot().apply(init).toMarkdownString())
fun textHtml(init: RichTextRoot.() -> Unit) =
    MarkdownText(RichTextRoot().apply(init).toHtmlString())
fun textWithEntities(init: RichTextRoot.() -> Unit) =
    RichTextRoot().apply(init).toTextWithEntities()

fun captionMarkdown(init: RichTextRoot.() -> Unit) =
    MarkdownCaption(RichTextRoot().apply(init).toMarkdownString())
fun captionHtml(init: RichTextRoot.() -> Unit) =
    MarkdownCaption(RichTextRoot().apply(init).toHtmlString())
fun captionWithEntities(init: RichTextRoot.() -> Unit) =
    RichTextRoot().apply(init).toCaptionWithEntities()

fun quoteMarkdown(init: RichTextRoot.() -> Unit) =
    MarkdownQuote(RichTextRoot().apply(init).toMarkdownString())
fun quoteHtml(init: RichTextRoot.() -> Unit) =
    MarkdownQuote(RichTextRoot().apply(init).toHtmlString())
fun quoteWithEntities(init: RichTextRoot.() -> Unit) =
    RichTextRoot().apply(init).toQuoteWithEntities()

fun explanationMarkdown(init: RichTextRoot.() -> Unit) =
    MarkdownExplanation(RichTextRoot().apply(init).toMarkdownString())
fun explanationHtml(init: RichTextRoot.() -> Unit) =
    MarkdownExplanation(RichTextRoot().apply(init).toHtmlString())
fun explanationWithEntities(init: RichTextRoot.() -> Unit) =
    RichTextRoot().apply(init).toExplanationWithEntities()

@DslMarker
private annotation class RichTextDslMarker

@RichTextDslMarker
abstract class RichTextElement internal constructor() {
    internal abstract fun StringBuilder.renderMarkdown()
    internal abstract fun FlowContent.renderHtml()
    internal abstract fun TextWithEntitiesBuilder.renderTextWithEntities()
}

class Text internal constructor(val value: String): RichTextElement() {
    override fun StringBuilder.renderMarkdown() {
        append(MarkdownTools.escapeText(value))
    }

    override fun FlowContent.renderHtml() {
        +value
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        append(text)
    }
}


abstract class RichTextElementWithChildren internal constructor() : RichTextElement() {
    protected val children: MutableList<RichTextElement> = mutableListOf()
    abstract val entityType: MessageEntityType

    protected fun <T : RichTextElement> initTag(tag: T, init: T.() -> Unit = {}): T {
        tag.init()
        children.add(tag)
        return tag
    }

    internal open fun StringBuilder.renderMarkdownChildren() {
        for (child in children) {
            with (child) {
                renderMarkdown()
            }
        }
    }

    internal open fun FlowContent.renderHtmlChildren() {
        for (child in children) {
            with (child) {
                renderHtml()
            }
        }
    }

    internal open fun TextWithEntitiesBuilder.renderTextWithEntitiesChildren() {
        for (child in children) {
            with (child) {
                renderTextWithEntities()
            }
        }
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        val offset = currentPosition
        renderTextWithEntitiesChildren()
        val length = currentPosition - offset
        append(offset = offset, length = length, entityType = entityType)
    }

    open fun text(string: String) = children.add(Text(string))
    open operator fun String.unaryPlus() = text(this)
    open fun space() = children.add(Text(" "))
    open fun br() = children.add(Text("\n"))

    protected open fun b(init: Bold.() -> Unit) = initTag(Bold(), init)
    protected open fun i(init: Italic.() -> Unit) = initTag(Italic(), init)
    protected open fun u(init: Underline.() -> Unit) = initTag(Underline(), init)
    protected open fun s(init: Strikethrough.() -> Unit) = initTag(Strikethrough(), init)
    protected open fun a(href: String, init: Link.() -> Unit) = initTag(
        Link(
            href
        ), init)
    protected open fun emoji(basic: String, customId: Long) = initTag(
        Emoji(
            basic,
            customId
        )
    )
    protected open fun user(user: User, init: UserMention.() -> Unit) =
        initTag(UserMention(user), init)
    protected open fun user(user: User, text: String) = initTag(UserMention(user)) { +text }
    protected open fun user(user: User) = when {
        user.username != null -> initTag(UserMention(user)) { +"@${user.username}" }
        user.fullName != null -> initTag(UserMention(user)) { +user.fullName!! }
        else -> initTag(UserMention(user)) { +"???" }
    }

    protected open fun userSoft(user: User) = when {
        user.username != null -> initTag(Code("@${user.username}"))
        user.fullName != null -> initTag(Code(user.fullName!!))
        else -> initTag(Code("???"))
    }
    protected open fun code(code: String) = initTag(Code(code))
    protected open fun pre(code: String, language: String? = null) = initTag(
        Pre(
            code,
            language
        )
    )
    protected open fun spoiler(init: Spoiler.() -> Unit) = initTag(Spoiler(), init)
    protected open fun blockQuote(init: BlockQuote.() -> Unit) = initTag(BlockQuote(), init)
}

abstract class WrappedRichTextElementWithChildren internal constructor() : RichTextElementWithChildren() {
    abstract fun markdownPrefix(builder: StringBuilder)
    open fun markdownPostfix(builder: StringBuilder) = markdownPrefix(builder)

    override fun StringBuilder.renderMarkdown() {
        markdownPrefix(this)
        this.renderMarkdownChildren()
        markdownPostfix(this)
    }

}

class Bold internal constructor() : WrappedRichTextElementWithChildren() {
    override fun markdownPrefix(builder: StringBuilder) {
        builder.append("*")
    }

    override fun FlowContent.renderHtml() {
        b {
            renderHtmlChildren()
        }
    }

    override val entityType: MessageEntityType = MessageEntityType.BOLD

    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun user(user: User, init: UserMention.() -> Unit) = super.user(user, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Italic internal constructor(
    // See notes here: https://core.telegram.org/bots/api#markdownv2-style
    // We could be more smart here and add extra char only when italic is the last part of underline
    private val insideUnderline: Boolean = false
) : WrappedRichTextElementWithChildren() {
    override fun markdownPrefix(builder: StringBuilder) {
        builder.append("_")
    }

    override fun markdownPostfix(builder: StringBuilder) {
        builder.append("_")
        if (insideUnderline) {
            builder.append("\r")
        }
    }

    override fun FlowContent.renderHtml() {
        i {
            renderHtmlChildren()
        }
    }

    override val entityType: MessageEntityType = MessageEntityType.ITALIC

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, init: UserMention.() -> Unit) = super.user(user, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Underline internal constructor() : WrappedRichTextElementWithChildren() {
    override fun markdownPrefix(builder: StringBuilder) {
        builder.append("__")
    }

    override fun FlowContent.renderHtml() {
        u {
            renderHtmlChildren()
        }
    }

    override val entityType: MessageEntityType = MessageEntityType.UNDERLINE

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = initTag(
        Italic(
            true
        ), init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun user(user: User, init: UserMention.() -> Unit) = super.user(user, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Strikethrough internal constructor(): WrappedRichTextElementWithChildren() {
    override fun markdownPrefix(builder: StringBuilder) {
        builder.append("~")
    }

    override fun FlowContent.renderHtml() {
        s {
            renderHtmlChildren()
        }
    }

    override val entityType: MessageEntityType = MessageEntityType.STRIKETHROUGH

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun user(user: User, init: UserMention.() -> Unit) = super.user(user, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Link internal constructor(private val href: String) : RichTextElementWithChildren() {
    override val entityType: MessageEntityType = MessageEntityType.TEXT_LINK

    override fun StringBuilder.renderMarkdown() {
        append("[")
        this.renderMarkdownChildren()
        append("]")
        append("(${MarkdownTools.escapeUrl(href)})")
    }

    override fun FlowContent.renderHtml() {
        a(href = href) {
            renderHtmlChildren()
        }
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        val offset = currentPosition
        renderTextWithEntitiesChildren()
        val length = currentPosition - offset
        append(offset = offset, length = length, entityType = entityType, url = href)
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)

}

class UserMention internal constructor(private val user: User) : RichTextElementWithChildren() {
    override val entityType: MessageEntityType = MessageEntityType.MENTION
    private val href = "tg://user?id=${user.id}"

    override fun StringBuilder.renderMarkdown() {
        append("[")
        this.renderMarkdownChildren()
        append("]")
        append("(${MarkdownTools.escapeUrl(href)})")
    }

    override fun FlowContent.renderHtml() {
        a(href = href) {
            renderHtmlChildren()
        }
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        val offset = currentPosition
        renderTextWithEntitiesChildren()
        val length = currentPosition - offset

        val onlyTextChild = if (children.size == 1) children.first() as? Text else null

        val entityType = if (null != user.username && onlyTextChild?.value == "@{${user.username}") {
            MessageEntityType.MENTION
        } else {
            MessageEntityType.TEXT_MENTION
        }

        append(offset = offset, length = length, entityType = entityType, user = user)
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)

}

class Emoji internal constructor(private val basic: String, private val customId: Long) : RichTextElement() {
    override fun StringBuilder.renderMarkdown() {
        append("[$basic]")
        append("(tg://emoji?id=$customId})")
    }

    override fun FlowContent.renderHtml() {
        with (TelegramHtmlEx) {
            tgEmoji(emojiId = customId) {
                +basic
            }
        }
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        append(string = basic, entityType = MessageEntityType.CUSTOM_EMOJI, customEmojiId = customId.toString())
    }
}

class Code internal constructor(private val code: String) : RichTextElement() {
    override fun StringBuilder.renderMarkdown() {
        append("`${MarkdownTools.escapeCode(code)}`")
    }

    override fun FlowContent.renderHtml() {
        code {
            +code
        }
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        append(string = code, entityType = MessageEntityType.CODE)
    }
}

class Pre internal constructor(private val code: String, private val language: String? = null) : RichTextElement() {
    override fun StringBuilder.renderMarkdown() {
        append("\n```")
        language?.let {
            append(MarkdownTools.escapeCode(language))
        }
        append("\n")
        append(MarkdownTools.escapeCode(code))
        append("```\n")
    }

    override fun FlowContent.renderHtml() {
        if (null == language) {
            pre {
                +code
            }
        } else {
            pre {
                code {
                    this@code.attributes["language"] = language
                    +code
                }
            }
        }
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        append(string = code, entityType = MessageEntityType.PRE, language = language)
    }
}

class Spoiler internal constructor() : WrappedRichTextElementWithChildren() {
    override val entityType: MessageEntityType = MessageEntityType.SPOILER
    override fun markdownPrefix(builder: StringBuilder) {
        builder.append("||")
    }

    override fun FlowContent.renderHtml() {
        span(classes = "tg-spoiler") {
            renderHtmlChildren()
        }
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, init: UserMention.() -> Unit) = super.user(user, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
}

class BlockQuote internal constructor() : RichTextElementWithChildren() {
    override val entityType: MessageEntityType = MessageEntityType.BLOCKQUOTE

    override fun StringBuilder.renderMarkdown() {
        val childBuilder = StringBuilder("\n>")
        childBuilder.renderMarkdownChildren()
        childBuilder.replace(Regex("""\n"""), "\n>")
        childBuilder.append("\n")
        append(childBuilder.toString())
    }

    override fun FlowContent.renderHtml() {
        blockQuote {
            renderHtmlChildren()
        }
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, init: UserMention.() -> Unit) = super.user(user, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun userSoft(user: User) = super.userSoft(user)
    public override fun code(code: String) = super.code(code)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class RichTextRoot internal constructor() : RichTextElementWithChildren() {
    override val entityType: MessageEntityType
        get() = error("Should not be called")

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, init: UserMention.() -> Unit) = super.user(user, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun userSoft(user: User) = super.userSoft(user)
    public override fun code(code: String) = super.code(code)
    public override fun pre(code: String, language: String?) = super.pre(code, language)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
    public override fun blockQuote(init: BlockQuote.() -> Unit) = super.blockQuote(init)

    override fun StringBuilder.renderMarkdown() {
        this.renderMarkdownChildren()
    }

    override fun FlowContent.renderHtml() {
        renderHtmlChildren()
    }

    override fun TextWithEntitiesBuilder.renderTextWithEntities() {
        renderTextWithEntitiesChildren()
    }

    fun toMarkdownString() = StringBuilder().apply {
        this.renderMarkdown()
    }.toString()

    fun toHtmlString(): String {
        val html = StringBuilder().appendHTML(prettyPrint = false).body {
            renderHtmlChildren()
        }.toString()
        return html.trim().removePrefix("<body>").removeSuffix("</body>")
    }

    fun toTextWithEntities(): TextWithEntities =
        TextWithEntitiesBuilder().let {
            it.renderTextWithEntities()
            TextWithEntities(text = it.text, entities = it.entities)
        }

    fun toCaptionWithEntities(): CaptionWithEntities =
        TextWithEntitiesBuilder().let {
            it.renderTextWithEntities()
            CaptionWithEntities(caption = it.text, captionEntities = it.entities)
        }

    fun toQuoteWithEntities(): QuoteWithEntities =
        TextWithEntitiesBuilder().let {
            it.renderTextWithEntities()
            QuoteWithEntities(quote = it.text, quoteEntities = it.entities)
        }

    fun toExplanationWithEntities(): ExplanationWithEntities =
        TextWithEntitiesBuilder().let {
            it.renderTextWithEntities()
            ExplanationWithEntities(explanation = it.text, explanationEntities = it.entities)
        }

}

private object TelegramHtmlEx {
    class TgEmoji(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*>) :
        HTMLTag("tg-emoji", consumer, initialAttributes, null, true, false),
        HtmlBlockInlineTag
    inline fun FlowOrPhrasingContent.tgEmoji(emojiId: Long, crossinline block : TgEmoji.() -> Unit = {}) : Unit = TgEmoji(
        attributesMapOf("emojiId", emojiId.toString()), consumer).visit(block)
}

private object MarkdownTools {
    fun escapeText(string: String) =
        string.replace("""([_*\[\]()~`>#+\-=|{}.!\\])""".toRegex(), """\\$1""")

    fun escapeUrl(string: String) =
        string.replace("""([)\\])""".toRegex(), """\\$1""")

    fun escapeCode(string: String) =
        string.replace("""([`\\.])""".toRegex(), """\\$1""")
}

private val User.fullName: String?
    get() {
        val firstName = this.firstName ?: ""
        val lastName = this.lastName ?: ""
        val fullName = "$firstName $lastName".trim()
        return fullName.ifBlank { null }
    }

internal class TextWithEntitiesBuilder {
    private val textBuilder = StringBuilder()
    private val entitiesBuilder = mutableListOf<MessageEntity>()

    fun append(string: String) {
        textBuilder.append(string)
    }

    fun append(
        string: String,
        entityType: MessageEntityType,
        url: String? = null,
        user: User? = null,
        language: String? = null,
        customEmojiId: String? = null
    ) {
        val start = textBuilder.length
        textBuilder.append(string)
        entitiesBuilder.add(
            MessageEntity(
                type = entityType, offset = start, length = string.length,
                url = url, user = user, customEmojiId = customEmojiId
            )
        )
    }

    fun append(
        offset: Int,
        length: Int,
        entityType: MessageEntityType,
        url: String? = null,
        user: User? = null,
        language: String? = null,
        customEmojiId: String? = null
    ) {
        entitiesBuilder.add(
            MessageEntity(
                type = entityType, offset = offset, length = length,
                url = url, user = user, customEmojiId = customEmojiId
            )
        )
    }

    val currentPosition
        get() = textBuilder.length

    val text: String
        get() = textBuilder.toString()

    val entities: List<MessageEntity>
        get() = entitiesBuilder
}