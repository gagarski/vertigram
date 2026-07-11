package ski.gagar.vertigram.telegram.markup

import kotlinx.html.FlowContent
import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.HTMLTag
import kotlinx.html.HtmlBlockInlineTag
import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.attributesMapOf
import kotlinx.html.b
import kotlinx.html.body
import kotlinx.html.br
import kotlinx.html.cite
import kotlinx.html.code
import kotlinx.html.figcaption
import kotlinx.html.figure
import kotlinx.html.footer
import kotlinx.html.hr
import kotlinx.html.i
import kotlinx.html.img
import kotlinx.html.ol
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.stream.appendHTML
import kotlinx.html.u
import kotlinx.html.ul
import kotlinx.html.visit
import ski.gagar.vertigram.telegram.types.InputRichMessage
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.chrono.ChronoZonedDateTime

/**
 * Build [InputRichMessage.Html] using rich-message HTML formatting.
 *
 * @sample ski.gagar.vertigram.samples.richMessageSample
 */
fun richMessageHtml(
    @Suppress("UNUSED_PARAMETER")
    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    isRtl: Boolean = false,
    skipEntityDetection: Boolean = false,
    init: RichMessageBuilder.() -> Unit
) = InputRichMessage.Html(
    html = RichMessageBuilder().apply(init).toHtmlString(),
    isRtl = isRtl,
    skipEntityDetection = skipEntityDetection
)

/**
 * Build [InputRichMessage.Markdown] using rich-message Markdown formatting.
 *
 * @sample ski.gagar.vertigram.samples.richMessageMarkdownSample
 */
fun richMessageMarkdown(
    @Suppress("UNUSED_PARAMETER")
    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    isRtl: Boolean = false,
    skipEntityDetection: Boolean = false,
    init: RichMessageBuilder.() -> Unit
) = InputRichMessage.Markdown(
    markdown = RichMessageBuilder().apply(init).toMarkdownString(),
    isRtl = isRtl,
    skipEntityDetection = skipEntityDetection
)

/**
 * Build [InputRichMessage.Markdown] from raw rich-message Markdown.
 */
fun richMessageMarkdown(
    markdown: String,
    @Suppress("UNUSED_PARAMETER")
    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    isRtl: Boolean = false,
    skipEntityDetection: Boolean = false
) = InputRichMessage.Markdown(
    markdown = markdown,
    isRtl = isRtl,
    skipEntityDetection = skipEntityDetection
)

/**
 * Build [InputRichMessage.Html] from raw rich-message HTML.
 */
fun richMessageHtml(
    html: String,
    @Suppress("UNUSED_PARAMETER")
    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    isRtl: Boolean = false,
    skipEntityDetection: Boolean = false
) = InputRichMessage.Html(
    html = html,
    isRtl = isRtl,
    skipEntityDetection = skipEntityDetection
)

@DslMarker
private annotation class RichMessageDslMarker

@RichMessageDslMarker
open class RichInlineBuilder internal constructor() {
    internal val children = mutableListOf<RichInlineElement>()

    fun text(value: String) {
        children.add(RichInlineElement.Text(value))
    }

    operator fun String.unaryPlus() = text(this)

    fun space() {
        text(" ")
    }

    fun br() {
        children.add(RichInlineElement.Br)
    }

    fun b(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Bold(RichInlineBuilder().apply(init).children))

    fun i(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Italic(RichInlineBuilder().apply(init).children))

    fun u(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Underline(RichInlineBuilder().apply(init).children))

    fun s(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Strikethrough(RichInlineBuilder().apply(init).children))

    fun mark(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Mark(RichInlineBuilder().apply(init).children))

    fun sub(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Subscript(RichInlineBuilder().apply(init).children))

    fun sup(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Superscript(RichInlineBuilder().apply(init).children))

    fun spoiler(init: RichInlineBuilder.() -> Unit) =
        children.add(RichInlineElement.Spoiler(RichInlineBuilder().apply(init).children))

    fun code(value: String) {
        children.add(RichInlineElement.Code(value))
    }

    fun math(expression: String) {
        children.add(RichInlineElement.Math(expression))
    }

    fun a(href: String, init: RichInlineBuilder.() -> Unit) {
        children.add(RichInlineElement.Link(href, RichInlineBuilder().apply(init).children))
    }

    fun referenceLink(name: String, init: RichInlineBuilder.() -> Unit) =
        a("#$name", init)

    fun anchor(name: String) {
        children.add(RichInlineElement.Anchor(name))
    }

    fun reference(name: String, init: RichInlineBuilder.() -> Unit) {
        children.add(RichInlineElement.Reference(name, RichInlineBuilder().apply(init).children))
    }

    fun email(emailAddress: String, init: RichInlineBuilder.() -> Unit) =
        a("mailto:$emailAddress", init)

    fun phone(phoneNumber: String, init: RichInlineBuilder.() -> Unit) =
        a("tel:$phoneNumber", init)

    fun user(user: User, init: RichInlineBuilder.() -> Unit) =
        a("tg://user?id=${user.id}", init)

    fun user(user: User, text: String) = user(user) { +text }

    fun emoji(alternativeText: String, customId: Long) {
        children.add(RichInlineElement.Emoji(alternativeText, customId))
    }

    fun dateTime(text: String, unixTime: Instant, dateTimeFormat: DateTimeFormat? = null) {
        children.add(RichInlineElement.DateTime(text, unixTime, dateTimeFormat))
    }

    fun dateTime(text: String, dateTime: OffsetDateTime, dateTimeFormat: DateTimeFormat? = null) =
        dateTime(text, dateTime.toInstant(), dateTimeFormat)

    fun dateTime(text: String, dateTime: ZonedDateTime, dateTimeFormat: DateTimeFormat? = null) =
        dateTime(text, dateTime.toInstant(), dateTimeFormat)

    fun dateTime(text: String, dateTime: ChronoZonedDateTime<*>, dateTimeFormat: DateTimeFormat? = null) =
        dateTime(text, dateTime.toInstant(), dateTimeFormat)
}

class RichMessageBuilder internal constructor() : RichBlockContainerBuilder()

@RichMessageDslMarker
open class RichBlockContainerBuilder internal constructor() {
    internal val blocks = mutableListOf<RichBlockElement>()

    fun text(value: String) = paragraph { +value }
    fun paragraph(init: RichInlineBuilder.() -> Unit) {
        blocks.add(RichBlockElement.Paragraph(RichInlineBuilder().apply(init).children))
    }

    fun heading(level: Int, init: RichInlineBuilder.() -> Unit) {
        require(level in 1..6) {
            "Heading level must be in range 1..6"
        }
        blocks.add(RichBlockElement.Heading(level, RichInlineBuilder().apply(init).children))
    }

    fun h1(init: RichInlineBuilder.() -> Unit) = heading(1, init)
    fun h2(init: RichInlineBuilder.() -> Unit) = heading(2, init)
    fun h3(init: RichInlineBuilder.() -> Unit) = heading(3, init)
    fun h4(init: RichInlineBuilder.() -> Unit) = heading(4, init)
    fun h5(init: RichInlineBuilder.() -> Unit) = heading(5, init)
    fun h6(init: RichInlineBuilder.() -> Unit) = heading(6, init)

    fun pre(code: String, language: String? = null) {
        blocks.add(RichBlockElement.Pre(code, language))
    }

    fun footer(init: RichInlineBuilder.() -> Unit) {
        blocks.add(RichBlockElement.Footer(RichInlineBuilder().apply(init).children))
    }
    fun divider() = blocks.add(RichBlockElement.Divider)
    fun mathBlock(expression: String) = blocks.add(RichBlockElement.Math(expression))
    fun anchor(name: String) = blocks.add(RichBlockElement.Anchor(name))

    fun unorderedList(init: RichListBuilder.() -> Unit) {
        blocks.add(RichBlockElement.ListBlock(false, null, null, false, RichListBuilder().apply(init).items))
    }

    fun orderedList(
        start: Int? = null,
        type: String? = null,
        reversed: Boolean = false,
        init: RichListBuilder.() -> Unit
    ) {
        blocks.add(RichBlockElement.ListBlock(true, start, type, reversed, RichListBuilder().apply(init).items))
    }

    fun blockQuote(
        credit: (RichInlineBuilder.() -> Unit)? = null,
        init: RichBlockContainerBuilder.() -> Unit
    ) {
        blocks.add(
            RichBlockElement.BlockQuote(
                blocks = RichBlockContainerBuilder().apply(init).blocks,
                credit = credit?.let { RichInlineBuilder().apply(it).children }
            )
        )
    }

    fun pullQuote(
        credit: (RichInlineBuilder.() -> Unit)? = null,
        init: RichInlineBuilder.() -> Unit
    ) {
        blocks.add(
            RichBlockElement.PullQuote(
                text = RichInlineBuilder().apply(init).children,
                credit = credit?.let { RichInlineBuilder().apply(it).children }
            )
        )
    }

    fun details(
        open: Boolean = false,
        summary: RichInlineBuilder.() -> Unit,
        init: RichBlockContainerBuilder.() -> Unit
    ) {
        blocks.add(
            RichBlockElement.Details(
                open = open,
                summary = RichInlineBuilder().apply(summary).children,
                blocks = RichBlockContainerBuilder().apply(init).blocks
            )
        )
    }

    fun photo(url: String, spoiler: Boolean = false, caption: (RichCaptionBuilder.() -> Unit)? = null) {
        blocks.add(RichBlockElement.Photo(url, spoiler, caption?.let { RichCaptionBuilder().apply(it).caption() }))
    }

    fun video(url: String, spoiler: Boolean = false, caption: (RichCaptionBuilder.() -> Unit)? = null) {
        blocks.add(RichBlockElement.Video(url, spoiler, caption?.let { RichCaptionBuilder().apply(it).caption() }))
    }

    fun audio(url: String, caption: (RichCaptionBuilder.() -> Unit)? = null) {
        blocks.add(RichBlockElement.Audio(url, caption?.let { RichCaptionBuilder().apply(it).caption() }))
    }

    fun map(
        latitude: Double,
        longitude: Double,
        zoom: Int,
        caption: (RichCaptionBuilder.() -> Unit)? = null
    ) {
        blocks.add(
            RichBlockElement.Map(
                latitude = latitude,
                longitude = longitude,
                zoom = zoom,
                caption = caption?.let { RichCaptionBuilder().apply(it).caption() }
            )
        )
    }

    fun collage(caption: (RichCaptionBuilder.() -> Unit)? = null, init: RichMediaGroupBuilder.() -> Unit) {
        mediaGroup("tg-collage", caption, init)
    }

    fun slideshow(caption: (RichCaptionBuilder.() -> Unit)? = null, init: RichMediaGroupBuilder.() -> Unit) {
        mediaGroup("tg-slideshow", caption, init)
    }

    fun table(
        bordered: Boolean = false,
        striped: Boolean = false,
        caption: (RichInlineBuilder.() -> Unit)? = null,
        init: RichTableBuilder.() -> Unit
    ) {
        blocks.add(
            RichBlockElement.Table(
                bordered = bordered,
                striped = striped,
                caption = caption?.let { RichInlineBuilder().apply(it).children },
                rows = RichTableBuilder().apply(init).rows
            )
        )
    }

    fun thinking(init: RichInlineBuilder.() -> Unit) {
        blocks.add(RichBlockElement.Thinking(RichInlineBuilder().apply(init).children))
    }

    internal fun toHtmlString(): String {
        val html = StringBuilder().appendHTML(prettyPrint = false).body {
            renderHtmlBlocks(blocks)
        }.toString()
        return html.trim().removePrefix("<body>").removeSuffix("</body>")
    }

    internal fun toMarkdownString() = StringBuilder().apply {
        renderMarkdownBlocks(blocks)
    }.toString().trimEnd()

    private fun mediaGroup(tag: String, caption: (RichCaptionBuilder.() -> Unit)?, init: RichMediaGroupBuilder.() -> Unit) {
        blocks.add(
            RichBlockElement.MediaGroup(
                tag = tag,
                media = RichMediaGroupBuilder().apply(init).media,
                caption = caption?.let { RichCaptionBuilder().apply(it).caption() }
            )
        )
    }
}

class RichListBuilder internal constructor() {
    internal val items = mutableListOf<RichListItem>()

    fun item(
        value: String? = null,
        checked: Boolean? = null,
        init: RichBlockContainerBuilder.() -> Unit
    ) {
        items.add(
            RichListItem(
                value = value,
                checked = checked,
                blocks = RichBlockContainerBuilder().apply(init).blocks
            )
        )
    }
}

class RichCaptionBuilder internal constructor() : RichInlineBuilder() {
    internal var credit: List<RichInlineElement>? = null

    fun credit(init: RichInlineBuilder.() -> Unit) {
        credit = RichInlineBuilder().apply(init).children
    }

    internal fun caption() = RichCaption(children, credit)
}

class RichMediaGroupBuilder internal constructor() {
    internal val media = mutableListOf<RichGroupMedia>()

    fun photo(url: String, spoiler: Boolean = false) {
        media.add(RichGroupMedia.Photo(url, spoiler))
    }

    fun video(url: String, spoiler: Boolean = false) {
        media.add(RichGroupMedia.Video(url, spoiler))
    }
}

class RichTableBuilder internal constructor() {
    internal val rows = mutableListOf<List<RichTableCell>>()

    fun row(init: RichTableRowBuilder.() -> Unit) {
        rows.add(RichTableRowBuilder().apply(init).cells)
    }
}

class RichTableRowBuilder internal constructor() {
    internal val cells = mutableListOf<RichTableCell>()

    fun cell(
        header: Boolean = false,
        colspan: Int? = null,
        rowspan: Int? = null,
        align: String? = null,
        verticalAlign: String? = null,
        init: RichInlineBuilder.() -> Unit
    ) {
        cells.add(
            RichTableCell(
                header = header,
                colspan = colspan,
                rowspan = rowspan,
                align = align,
                verticalAlign = verticalAlign,
                text = RichInlineBuilder().apply(init).children
            )
        )
    }
}

internal sealed class RichInlineElement {
    internal abstract fun StringBuilder.renderMarkdown()
    internal abstract fun FlowContent.renderHtml()

    data class Text(val value: String) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append(MarkdownTools.escapeText(value))
        }

        override fun FlowContent.renderHtml() {
            +value
        }
    }

    data object Br : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("\n")
        }

        override fun FlowContent.renderHtml() {
            br()
        }
    }

    abstract class Wrapped(
        private val markdownPrefix: String,
        private val markdownPostfix: String,
        private val wrappedChildren: kotlin.collections.List<RichInlineElement>
    ) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append(markdownPrefix)
            renderMarkdownChildren(wrappedChildren)
            append(markdownPostfix)
        }
    }

    data class Bold(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("**", "**", children) {
        override fun FlowContent.renderHtml() {
            b { renderHtmlChildren(children) }
        }
    }

    data class Italic(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("*", "*", children) {
        override fun FlowContent.renderHtml() {
            i { renderHtmlChildren(children) }
        }
    }

    data class Underline(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("<u>", "</u>", children) {
        override fun FlowContent.renderHtml() {
            u { renderHtmlChildren(children) }
        }
    }

    data class Strikethrough(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("~~", "~~", children) {
        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { s { renderHtmlChildren(children) } }
        }
    }

    data class Mark(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("==", "==", children) {
        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { mark { renderHtmlChildren(children) } }
        }
    }

    data class Subscript(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("<sub>", "</sub>", children) {
        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { sub { renderHtmlChildren(children) } }
        }
    }

    data class Superscript(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("<sup>", "</sup>", children) {
        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { sup { renderHtmlChildren(children) } }
        }
    }

    data class Spoiler(private val children: kotlin.collections.List<RichInlineElement>) : Wrapped("||", "||", children) {
        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { tgSpoiler { renderHtmlChildren(children) } }
        }
    }

    data class Code(val value: String) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("`")
            append(MarkdownTools.escapeCode(value))
            append("`")
        }

        override fun FlowContent.renderHtml() {
            code { +value }
        }
    }

    data class Math(val expression: String) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("<tg-math>${escapeHtml(expression)}</tg-math>")
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { tgMath { +expression } }
        }
    }

    data class Link(val href: String, val children: kotlin.collections.List<RichInlineElement>) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("[")
            renderMarkdownChildren(children)
            append("](${MarkdownTools.escapeUrl(href)})")
        }

        override fun FlowContent.renderHtml() {
            a(href = href) { renderHtmlChildren(children) }
        }
    }

    data class Anchor(val name: String) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("""<a name="${escapeHtmlAttribute(name)}"></a>""")
        }

        override fun FlowContent.renderHtml() {
            a { attributes["name"] = name }
        }
    }

    data class Reference(val name: String, val children: kotlin.collections.List<RichInlineElement>) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("""<tg-reference name="${escapeHtmlAttribute(name)}">""")
            renderMarkdownChildren(children)
            append("</tg-reference>")
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { tgReference(name) { renderHtmlChildren(children) } }
        }
    }

    data class Emoji(val alternativeText: String, val customId: Long) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("""<tg-emoji emoji-id="$customId">${escapeHtml(alternativeText)}</tg-emoji>""")
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { tgEmoji(customId) { +alternativeText } }
        }
    }

    data class DateTime(val text: String, val unixTime: Instant, val dateTimeFormat: DateTimeFormat?) : RichInlineElement() {
        override fun StringBuilder.renderMarkdown() {
            append("""<tg-time unix="${unixTime.epochSecond}"""")
            dateTimeFormat?.let {
                append(""" format="${escapeHtmlAttribute(it.value)}"""")
            }
            append(">${escapeHtml(text)}</tg-time>")
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { tgTime(unixTime, dateTimeFormat) { +text } }
        }
    }
}

internal sealed class RichBlockElement {
    internal abstract fun StringBuilder.renderMarkdown()
    internal abstract fun FlowContent.renderHtml()

    data class Paragraph(private val children: kotlin.collections.List<RichInlineElement>) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            renderMarkdownChildren(children)
            append("\n\n")
        }

        override fun FlowContent.renderHtml() {
            p { renderHtmlChildren(children) }
        }
    }

    data class Footer(private val children: kotlin.collections.List<RichInlineElement>) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            footer { renderHtmlChildren(children) }
        }
    }

    data class Thinking(private val children: kotlin.collections.List<RichInlineElement>) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { tgThinking { renderHtmlChildren(children) } }
        }
    }

    data class Heading(val level: Int, private val children: kotlin.collections.List<RichInlineElement>) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            append("#".repeat(level))
            append(" ")
            renderMarkdownChildren(children)
            append("\n\n")
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) {
                custom("h$level") { renderHtmlChildren(children) }
            }
        }
    }

    data class Pre(val code: String, val language: String?) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            append("```")
            language?.let { append(MarkdownTools.escapeCode(it)) }
            append("\n")
            append(MarkdownTools.escapeCode(code))
            append("\n```\n\n")
        }

        override fun FlowContent.renderHtml() {
            pre {
                if (language == null) {
                    +code
                } else {
                    code {
                        attributes["class"] = "language-$language"
                        +code
                    }
                }
            }
        }
    }

    data object Divider : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            append("---\n\n")
        }

        override fun FlowContent.renderHtml() {
            hr {}
        }
    }

    data class Math(val expression: String) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            append("<tg-math-block>${escapeHtml(expression)}</tg-math-block>\n\n")
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { tgMathBlock { +expression } }
        }
    }

    data class Anchor(val name: String) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            append("""<a name="${escapeHtmlAttribute(name)}"></a>""").append("\n\n")
        }

        override fun FlowContent.renderHtml() {
            a { attributes["name"] = name }
        }
    }

    data class ListBlock(
        val ordered: Boolean,
        val start: Int?,
        val type: String?,
        val reversed: Boolean,
        val items: kotlin.collections.List<RichListItem>
    ) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            items.forEachIndexed { ix, item ->
                val marker = if (ordered) "${start?.plus(ix) ?: ix + 1}. " else "- "
                append(marker)
                item.checked?.let {
                    append(if (it) "[x] " else "[ ] ")
                }
                val itemText = StringBuilder().apply { renderMarkdownBlocks(item.blocks) }.toString().trim()
                append(itemText.replace("\n", "\n  "))
                append("\n")
            }
            append("\n")
        }

        override fun FlowContent.renderHtml() {
            if (ordered) {
                ol {
                    this@ListBlock.start?.let { attributes["start"] = it.toString() }
                    this@ListBlock.type?.let { attributes["type"] = it }
                    if (reversed) attributes["reversed"] = ""
                    renderHtmlItems()
                }
            } else {
                ul {
                    renderHtmlItems()
                }
            }
        }

        private fun FlowContent.renderHtmlItems() {
            for (item in items) {
                item.renderHtml(this)
            }
        }
    }

    data class BlockQuote(val blocks: kotlin.collections.List<RichBlockElement>, val credit: kotlin.collections.List<RichInlineElement>?) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            val quote = StringBuilder().apply { renderMarkdownBlocks(blocks) }.toString().trimEnd()
            append(quote.lineSequence().joinToString("\n") { "> $it" })
            credit?.let {
                append("\n> - ")
                renderMarkdownChildren(it)
            }
            append("\n\n")
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) {
                custom("blockquote") {
                    renderHtmlBlocks(blocks)
                    credit?.let { cite { renderHtmlChildren(it) } }
                }
            }
        }
    }

    data class PullQuote(val text: kotlin.collections.List<RichInlineElement>, val credit: kotlin.collections.List<RichInlineElement>?) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) {
                aside {
                    renderHtmlChildren(text)
                    credit?.let { cite { renderHtmlChildren(it) } }
                }
            }
        }
    }

    data class Details(val open: Boolean, val summary: kotlin.collections.List<RichInlineElement>, val blocks: kotlin.collections.List<RichBlockElement>) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) {
                details(open) {
                    summary { renderHtmlChildren(summary) }
                    renderHtmlBlocks(blocks)
                }
            }
        }
    }

    abstract class Media(private val caption: RichCaption?) : RichBlockElement() {
        protected abstract fun FlowContent.renderMedia()

        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            if (caption == null) {
                renderMedia()
            } else {
                figure {
                    renderMedia()
                    caption.renderHtml(this)
                }
            }
        }
    }

    data class Photo(val url: String, val spoiler: Boolean, val caption: RichCaption?) : Media(caption) {
        override fun FlowContent.renderMedia() {
            img(src = url) {
                if (spoiler) attributes["tg-spoiler"] = ""
            }
        }
    }

    data class Video(val url: String, val spoiler: Boolean, val caption: RichCaption?) : Media(caption) {
        override fun FlowContent.renderMedia() {
            with(RichMessageHtmlEx) { video(url, spoiler) }
        }
    }

    data class Audio(val url: String, val caption: RichCaption?) : Media(caption) {
        override fun FlowContent.renderMedia() {
            with(RichMessageHtmlEx) { audio(url) }
        }
    }

    data class Map(val latitude: Double, val longitude: Double, val zoom: Int, val caption: RichCaption?) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            if (caption == null) {
                renderMap()
            } else {
                figure {
                    renderMap()
                    caption.renderHtml(this)
                }
            }
        }

        private fun FlowContent.renderMap() {
            with(RichMessageHtmlEx) { tgMap(latitude, longitude, zoom) }
        }
    }

    data class MediaGroup(val tag: String, val media: kotlin.collections.List<RichGroupMedia>, val caption: RichCaption?) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) {
                custom(tag) {
                    for (item in media) {
                        with(item) { renderHtml() }
                    }
                    caption?.renderHtml(this)
                }
            }
        }
    }

    data class Table(
        val bordered: Boolean,
        val striped: Boolean,
        val caption: kotlin.collections.List<RichInlineElement>?,
        val rows: kotlin.collections.List<kotlin.collections.List<RichTableCell>>
    ) : RichBlockElement() {
        override fun StringBuilder.renderMarkdown() {
            htmlBlock { renderHtml() }
        }

        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) {
                custom("table") {
                    if (bordered) attributes["bordered"] = ""
                    if (striped) attributes["striped"] = ""
                    caption?.let {
                        custom("caption") { renderHtmlChildren(it) }
                    }
                    for (row in rows) {
                        custom("tr") {
                            for (cell in row) {
                                cell.renderHtml(this)
                            }
                        }
                    }
                }
            }
        }
    }
}

internal data class RichListItem(
    val value: String?,
    val checked: Boolean?,
    val blocks: kotlin.collections.List<RichBlockElement>
) {
    fun renderHtml(content: FlowContent) {
        with(RichMessageHtmlEx) {
            content.custom("li") {
                value?.let { attributes["value"] = it }
                checked?.let { checkbox(it) }
                renderHtmlBlocks(blocks)
            }
        }
    }
}

internal data class RichCaption(
    val text: kotlin.collections.List<RichInlineElement>,
    val credit: kotlin.collections.List<RichInlineElement>?
) {
    fun renderHtml(content: FlowContent) {
        content.figcaption {
            renderHtmlChildren(text)
            credit?.let { cite { renderHtmlChildren(it) } }
        }
    }
}

internal sealed class RichGroupMedia {
    internal abstract fun FlowContent.renderHtml()

    data class Photo(val url: String, val spoiler: Boolean) : RichGroupMedia() {
        override fun FlowContent.renderHtml() {
            img(src = url) {
                if (spoiler) attributes["tg-spoiler"] = ""
            }
        }
    }

    data class Video(val url: String, val spoiler: Boolean) : RichGroupMedia() {
        override fun FlowContent.renderHtml() {
            with(RichMessageHtmlEx) { video(url, spoiler) }
        }
    }
}

internal data class RichTableCell(
    val header: Boolean,
    val colspan: Int?,
    val rowspan: Int?,
    val align: String?,
    val verticalAlign: String?,
    val text: kotlin.collections.List<RichInlineElement>
) {
    fun renderHtml(content: FlowContent) {
        with(RichMessageHtmlEx) {
            content.custom(if (header) "th" else "td") {
                colspan?.let { attributes["colspan"] = it.toString() }
                rowspan?.let { attributes["rowspan"] = it.toString() }
                align?.let { attributes["align"] = it }
                verticalAlign?.let { attributes["valign"] = it }
                renderHtmlChildren(text)
            }
        }
    }
}

private fun StringBuilder.renderMarkdownChildren(children: kotlin.collections.List<RichInlineElement>) {
    for (child in children) {
        with(child) { renderMarkdown() }
    }
}

private fun StringBuilder.renderMarkdownBlocks(blocks: kotlin.collections.List<RichBlockElement>) {
    for (block in blocks) {
        with(block) { renderMarkdown() }
    }
}

private fun StringBuilder.htmlBlock(render: FlowContent.() -> Unit) {
    append(
        StringBuilder()
            .appendHTML(prettyPrint = false)
            .body { render() }
            .toString()
            .trim()
            .removePrefix("<body>")
            .removeSuffix("</body>")
    )
    append("\n\n")
}

private fun FlowContent.renderHtmlChildren(children: kotlin.collections.List<RichInlineElement>) {
    for (child in children) {
        with(child) { renderHtml() }
    }
}

private fun FlowContent.renderHtmlBlocks(blocks: kotlin.collections.List<RichBlockElement>) {
    for (block in blocks) {
        with(block) { renderHtml() }
    }
}

private object RichMessageHtmlEx {
    class CustomTag(name: String, initialAttributes: Map<String, String>, override val consumer: TagConsumer<*>) :
        HTMLTag(name, consumer, initialAttributes, null, true, false),
        HtmlBlockInlineTag

    inline fun FlowContent.custom(
        name: String,
        attrs: Map<String, String> = emptyMap(),
        crossinline block: CustomTag.() -> Unit = {}
    ) = CustomTag(name, attrs, consumer).visit(block)

    inline fun FlowContent.tgThinking(crossinline block: CustomTag.() -> Unit = {}) =
        custom("tg-thinking", block = block)

    inline fun FlowContent.tgMath(crossinline block: CustomTag.() -> Unit = {}) =
        custom("tg-math", block = block)

    inline fun FlowContent.tgMathBlock(crossinline block: CustomTag.() -> Unit = {}) =
        custom("tg-math-block", block = block)

    inline fun FlowContent.tgSpoiler(crossinline block: CustomTag.() -> Unit = {}) =
        custom("tg-spoiler", block = block)

    inline fun FlowContent.mark(crossinline block: CustomTag.() -> Unit = {}) =
        custom("mark", block = block)

    inline fun FlowContent.sub(crossinline block: CustomTag.() -> Unit = {}) =
        custom("sub", block = block)

    inline fun FlowContent.sup(crossinline block: CustomTag.() -> Unit = {}) =
        custom("sup", block = block)

    inline fun FlowContent.s(crossinline block: CustomTag.() -> Unit = {}) =
        custom("s", block = block)

    inline fun FlowContent.aside(crossinline block: CustomTag.() -> Unit = {}) =
        custom("aside", block = block)

    inline fun FlowContent.details(open: Boolean, crossinline block: CustomTag.() -> Unit = {}) {
        val attrs = if (open) mapOf("open" to "") else emptyMap()
        custom("details", attrs, block)
    }

    inline fun FlowContent.summary(crossinline block: CustomTag.() -> Unit = {}) =
        custom("summary", block = block)

    fun FlowContent.checkbox(checked: Boolean) {
        val attrs = mutableMapOf("type" to "checkbox")
        if (checked) attrs["checked"] = ""
        CustomTag("input", attrs, consumer).visit {}
    }

    fun FlowContent.video(url: String, spoiler: Boolean) {
        val attrs = mutableMapOf("src" to url)
        if (spoiler) attrs["tg-spoiler"] = ""
        CustomTag("video", attrs, consumer).visit {}
    }

    fun FlowContent.audio(url: String) {
        CustomTag("audio", mapOf("src" to url), consumer).visit {}
    }

    fun FlowContent.tgMap(latitude: Double, longitude: Double, zoom: Int) {
        CustomTag(
            "tg-map",
            attributesMapOf(
                "lat", latitude.toString(),
                "long", longitude.toString(),
                "zoom", zoom.toString()
            ),
            consumer
        ).visit {}
    }

    inline fun FlowContent.tgReference(name: String, crossinline block: CustomTag.() -> Unit = {}) =
        custom("tg-reference", attributesMapOf("name", name), block)

    inline fun FlowOrPhrasingContent.tgEmoji(emojiId: Long, crossinline block: CustomTag.() -> Unit = {}) =
        CustomTag("tg-emoji", attributesMapOf("emoji-id", emojiId.toString()), consumer).visit(block)

    inline fun FlowOrPhrasingContent.tgTime(
        unixTime: Instant,
        dateTimeFormat: DateTimeFormat? = null,
        crossinline block: CustomTag.() -> Unit = {}
    ) {
        val attrs = mutableMapOf("unix" to unixTime.epochSecond.toString())
        dateTimeFormat?.let {
            attrs["format"] = it.value
        }
        CustomTag("tg-time", attrs, consumer).visit(block)
    }
}

private fun escapeHtml(value: String) =
    value
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")

private fun escapeHtmlAttribute(value: String) =
    escapeHtml(value).replace("\"", "&quot;")
