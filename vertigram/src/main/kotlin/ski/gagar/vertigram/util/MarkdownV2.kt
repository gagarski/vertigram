package ski.gagar.vertigram.util

import ski.gagar.vertigram.entities.User

fun String.escapeText() =
    this.replace("""([_*\[\]()~`>#+\-=|{}.!])""".toRegex(), """\\$1""")

fun String.escapeUrl() =
    this.replace("""([)\\])""".toRegex(), """\\$1""")

fun String.escapeCode() =
    this.replace("""([`\\.])""".toRegex(), """\\$1""")

@DslMarker
annotation class MdV2TagMarker

@MdV2TagMarker
abstract class MdV2Element {
    abstract fun render(builder: StringBuilder)

    override fun toString() = StringBuilder().apply {
        render(this)
    }.toString()
}

class Text(private val value: String): MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append(value.escapeText())
    }
}


class Raw(private val value: String): MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append(value)
    }
}
private val User.fullName: String?
    get() {
        val firstName = this.firstName ?: ""
        val lastName = this.lastName ?: ""
        val fullName = "$firstName $lastName".trim()
        return if (fullName.isBlank()) return null else fullName
    }


abstract class MdV2ElementWithChildren : MdV2Element() {
    private val children: MutableList<MdV2Element> = mutableListOf()

    private fun <T : MdV2Element> initTag(tag: T, init: T.() -> Unit = {}): T {
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

    inner class Unsafe: MdV2ElementWithChildren() {
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
        public override fun user(user: User, text: String) = this@MdV2ElementWithChildren.user(user, text)
        public override fun user(user: User) = this@MdV2ElementWithChildren.user(user)
        public override fun userSoft(user: User) = this@MdV2ElementWithChildren.userSoft(user)
        public override fun code(code: String) = this@MdV2ElementWithChildren.code(code)
        public override fun pre(code: String, language: String?) = this@MdV2ElementWithChildren.pre(code, language)
    }

    fun unsafe() = Unsafe()
}

abstract class WrappedMdV2ElementWithChildren : MdV2ElementWithChildren() {
    abstract fun renderPrefix(builder: StringBuilder)
    open fun renderPostfix(builder: StringBuilder) = renderPrefix(builder)

    override fun render(builder: StringBuilder) {
        renderPrefix(builder)
        renderChildren(builder)
        renderPostfix(builder)
    }

}

class Bold : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("*")
    }

    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
}

class Italic : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("_")
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
}

class Underline : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("__")
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun s(init: Strikethrough.() -> Unit) = super.s(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
}

class Strikethrough : WrappedMdV2ElementWithChildren() {
    override fun renderPrefix(builder: StringBuilder) {
        builder.append("~")
    }

    public override fun b(init: Bold.() -> Unit) = super.b(init)
    public override fun i(init: Italic.() -> Unit) = super.i(init)
    public override fun u(init: Underline.() -> Unit) = super.u(init)
    public override fun a(href: String, init: Link.() -> Unit) = super.a(href, init)
    public override fun user(user: User, text: String) = super.user(user, text)
    public override fun user(user: User) = super.user(user)
}

class Link(private val href: String) : MdV2ElementWithChildren() {
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

}

class Code(private val code: String) : MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append("`${code.escapeCode()}`")
    }

}

class Pre(private val code: String, private val language: String? = null) : MdV2Element() {
    override fun render(builder: StringBuilder) {
        builder.append("```")
        language?.let {
            builder.append(language.escapeCode()) // TODO should we escape here?
        }
        builder.append("\n")
        builder.append(code.escapeCode())
        builder.append("```")
    }

}

class Markdown : MdV2ElementWithChildren() {
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

    override fun render(builder: StringBuilder) {
        renderChildren(builder)
    }
}

fun md(init: Markdown.() -> Unit) = Markdown().apply(init)
