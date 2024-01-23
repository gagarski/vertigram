package ski.gagar.vxutil.vertigram.builders

import ski.gagar.vxutil.vertigram.types.User

@JvmInline
value class Markdown internal constructor(private val rendered: String) {
    override fun toString() = rendered
}
fun md(init: MarkdownRoot.() -> Unit) = Markdown(MarkdownRoot().apply(init).toString())

fun String.toMarkdown() = md { +this@toMarkdown }

private fun String.escapeText() =
    this.replace("""([_*\[\]()~`>#+\-=|{}.!\\])""".toRegex(), """\\$1""")

private fun String.escapeUrl() =
    this.replace("""([)\\])""".toRegex(), """\\$1""")

private fun String.escapeCode() =
    this.replace("""([`\\.])""".toRegex(), """\\$1""")

@DslMarker
annotation class MdV2TagMarker

@MdV2TagMarker
abstract class MdV2Element internal constructor() {
    abstract fun render(builder: StringBuilder)

    override fun toString() = StringBuilder().apply {
        render(this)
    }.toString()
}

class Text internal constructor(private val value: String): MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append(value.escapeText())
    }
}


class Raw internal constructor(private val value: String): MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append(value)
    }
}
private val User.fullName: String?
    get() {
        val firstName = this.firstName ?: ""
        val lastName = this.lastName ?: ""
        val fullName = "$firstName $lastName".trim()
        return fullName.ifBlank { null }
    }


abstract class MdV2ElementWithChildren internal constructor() : MdV2Element() {
    private val children: MutableList<MdV2Element> = mutableListOf()

    protected fun <T : MdV2Element> initTag(tag: T, init: T.() -> Unit = {}): T {
        tag.init()
        children.add(tag)
        return tag
    }

    open fun renderChildren(builder: StringBuilder) {
        for (child in children) {
            child.render(builder)
        }
    }
    open fun text(string: String) = children.add(Text(string))
    open operator fun String.unaryPlus() = text(this)
    open fun space() = children.add(Text(" "))
    open fun br() = children.add(Text("\n"))

    open fun raw(text: String) = children.add(Raw(text))

    protected open fun b(init: Bold.() -> Unit) = initTag(Bold(), init)
    protected open fun i(init: Italic.() -> Unit) = initTag(Italic(), init)
    protected open fun u(init: Underline.() -> Unit) = initTag(Underline(), init)
    protected open fun s(init: Strikethrough.() -> Unit) = initTag(Strikethrough(), init)
    protected open fun a(href: String, init: Link.() -> Unit) = initTag(Link(href), init)
    protected open fun emoji(basic: String, customId: Long) = initTag(Emoji(basic, customId))
    protected open fun user(user: User, text: String) = initTag(Link("tg://user?id=${user.id}")) { +text }
    protected open fun user(user: User) = when {
        user.username != null -> initTag(Text("@${user.username}"))
        user.fullName != null -> initTag(Link("tg://user?id=${user.id}")) { +user.fullName!! }
        else -> initTag(Link("tg://user?id=${user.id}")) { +"???" }
    }
    protected open fun userSoft(user: User) = when {
        user.username != null -> initTag(Code("@${user.username}"))
        user.fullName != null -> initTag(Code(user.fullName!!))
        else -> Code("???")
    }
    protected open fun code(code: String) = initTag(Code(code))
    protected open fun pre(code: String, language: String? = null) = initTag(Pre(code, language))
    protected open fun spoiler(init: Spoiler.() -> Unit) = initTag(Spoiler(), init)
    protected open fun blockQuote(init: BlockQuote.() -> Unit) = initTag(BlockQuote(), init)

    inner class Unsafe : MdV2ElementWithChildren() {
        override fun render(builder: StringBuilder) {
            this@MdV2ElementWithChildren.render(builder)
        }

        override fun text(string: String) = this@MdV2ElementWithChildren.text(string)
        override fun space() = this@MdV2ElementWithChildren.space()
        override fun br() = this@MdV2ElementWithChildren.br()

        public override fun b(init: Bold.() -> Unit) = this@MdV2ElementWithChildren.b(init)
        public override fun i(init: Italic.() -> Unit) = this@MdV2ElementWithChildren.i(init)
        public override fun u(init: Underline.() -> Unit) = this@MdV2ElementWithChildren.u(init)
        public override fun s(init: Strikethrough.() -> Unit) = this@MdV2ElementWithChildren.s(init)
        public override fun a(href: String, init: Link.() -> Unit) = this@MdV2ElementWithChildren.a(href, init)
        public override fun emoji(basic: String, customId: Long) = this@MdV2ElementWithChildren.emoji(basic, customId)
        public override fun user(user: User, text: String) = this@MdV2ElementWithChildren.user(user, text)
        public override fun user(user: User) = this@MdV2ElementWithChildren.user(user)
        public override fun userSoft(user: User) = this@MdV2ElementWithChildren.userSoft(user)
        public override fun code(code: String) = this@MdV2ElementWithChildren.code(code)
        public override fun pre(code: String, language: String?) = this@MdV2ElementWithChildren.pre(code, language)
        public override fun spoiler(init: Spoiler.() -> Unit) = this@MdV2ElementWithChildren.spoiler(init)
    }

    fun unsafe() = Unsafe()
}

abstract class WrappedMdV2ElementWithChildren internal constructor() : MdV2ElementWithChildren() {
    abstract fun renderPrefix(builder: StringBuilder)
    open fun renderPostfix(builder: StringBuilder) = renderPrefix(builder)

    override fun render(builder: StringBuilder) {
        renderPrefix(builder)
        renderChildren(builder)
        renderPostfix(builder)
    }

}

class Bold internal constructor() : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("*")
    }

    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Italic internal constructor(
    // See notes here: https://core.telegram.org/bots/api#markdownv2-style
    // We could be more smart here and add extra char only when italic is the last part of underline
    private val insideUnderline: Boolean = false
) : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("_")
    }

    override fun renderPostfix(builder: StringBuilder) {
        builder.append("_")
        if (insideUnderline) {
            builder.append("\r")
        }
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Underline internal constructor() : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("__")
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = initTag(Italic(true), init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Strikethrough internal constructor(): WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("~")
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
}

class Link internal constructor(private val href: String) : MdV2ElementWithChildren() {
    override fun render(builder: StringBuilder) {
        builder.append("[")
        renderChildren(builder)
        builder.append("]")
        builder.append("(${href.escapeUrl()})")
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun emoji(basic: String, customId: Long) = super.emoji(basic, customId)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)

}


class Emoji internal constructor(private val basic: String, private val customId: Long) : MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append("[$basic]")
        builder.append("(tg://emoji?id=$customId})")
    }
}



class Code internal constructor(private val code: String) : MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append("`${code.escapeCode()}`")
    }

}

class Pre internal constructor(private val code: String, private val language: String? = null) : MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append("\n```")
        language?.let {
            builder.append(language.escapeCode()) // TODO should we escape here?
        }
        builder.append("\n")
        builder.append(code.escapeCode())
        builder.append("```\n")
    }

}

class Spoiler internal constructor()  : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("||")
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
}

class BlockQuote internal constructor() : MdV2ElementWithChildren() {
    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun userSoft(user: User) = super.userSoft(user)
    public override fun code(code: String) = super.code(code)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)

    override fun render(builder: StringBuilder) {
        val childBuilder = StringBuilder("\n>")
        renderChildren(childBuilder)
        childBuilder.replace(Regex("""\n"""), "\n>")
        childBuilder.append("\n")
        builder.append(childBuilder.toString())
    }
}


class MarkdownRoot internal constructor() : MdV2ElementWithChildren() {
    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
    public override fun userSoft(user: User) = super.userSoft(user)
    public override fun code(code: String) = super.code(code)
    public override fun pre(code: String, language: String?) = super.pre(code, language)
    public override fun spoiler(init: Spoiler.() -> Unit) = super.spoiler(init)
    public override fun blockQuote(init: BlockQuote.() -> Unit) = super.blockQuote(init)

    override fun render(builder: StringBuilder) {
        renderChildren(builder)
    }
}