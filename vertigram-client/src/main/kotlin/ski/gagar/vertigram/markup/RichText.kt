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
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.User
import ski.gagar.vertigram.types.richtext.MarkdownV2Text
import ski.gagar.vertigram.types.richtext.TextWithEntities

fun String.toRichText() = TextWithEntities(this)

fun textMarkdown(init: RichTextRoot.() -> Unit) =
    MarkdownV2Text(RichTextRoot().apply(init).toMarkdownString())
fun textHtml(init: RichTextRoot.() -> Unit) =
    MarkdownV2Text(RichTextRoot().apply(init).toHtmlString())
fun textWithEntities(init: RichTextRoot.() -> Unit) =
    RichTextRoot().apply(init).toTextWithEntities()

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
    abstract fun createEntity(offset: Int, length: Int): MessageEntity

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
        append(createEntity(offset, length))
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

    override fun createEntity(offset: Int, length: Int): MessageEntity =
        MessageEntity.Bold(offset = offset, length = length)

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

    override fun createEntity(offset: Int, length: Int): MessageEntity =
        MessageEntity.Italic(offset = offset, length = length)

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
    override fun createEntity(offset: Int, length: Int): MessageEntity =
        MessageEntity.Underline(offset = offset, length = length)
    override fun markdownPrefix(builder: StringBuilder) {
        builder.append("__")
    }

    override fun FlowContent.renderHtml() {
        u {
            renderHtmlChildren()
        }
    }

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
    override fun createEntity(offset: Int, length: Int): MessageEntity =
        MessageEntity.Strikethrough(offset = offset, length = length)

    override fun markdownPrefix(builder: StringBuilder) {
        builder.append("~")
    }

    override fun FlowContent.renderHtml() {
        s {
            renderHtmlChildren()
        }
    }

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
    override fun createEntity(offset: Int, length: Int): MessageEntity =
        throw IllegalStateException("Do not call me")

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
        append(MessageEntity.TextLink(offset = offset, length = length, url = href))
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)

}

class UserMention internal constructor(private val user: User) : RichTextElementWithChildren() {
    override fun createEntity(offset: Int, length: Int): MessageEntity =
        MessageEntity.Mention(offset = offset, length = length)
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

        if (null != user.username && onlyTextChild?.value == "@{${user.username}") {
            append(MessageEntity.Mention(offset = offset, length = length))
        } else {
            append(MessageEntity.TextMention(offset = offset, length = length, user = user))
        }
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
        append(basic) { offset, length ->
            MessageEntity.CustomEmoji(offset = offset, length = length, customEmojiId = customId.toString())
        }
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
        append(code) { offset, length ->
            MessageEntity.Code(offset = offset, length = length)
        }
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
        append(code) { offset, length ->
            MessageEntity.Pre(offset = offset, length = length, language = language)
        }
    }
}

class Spoiler internal constructor() : WrappedRichTextElementWithChildren() {
    override fun createEntity(offset: Int, length: Int): MessageEntity =
        MessageEntity.Spoiler(offset = offset, length = length)

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
    override fun createEntity(offset: Int, length: Int): MessageEntity =
        MessageEntity.BlockQuote(offset = offset, length = length)

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
    override fun createEntity(offset: Int, length: Int): MessageEntity =
        throw IllegalStateException("Do not call me")

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
        entityCreator: (offset: Int, length: Int) -> MessageEntity
    ) {
        val start = textBuilder.length
        textBuilder.append(string)
        entitiesBuilder.add(
            entityCreator(start, string.length)
        )
    }

    fun append(
        entity: MessageEntity
    ) {
        entitiesBuilder.add(entity)
    }

    val currentPosition
        get() = textBuilder.length

    val text: String
        get() = textBuilder.toString()

    val entities: List<MessageEntity>
        get() = entitiesBuilder
}