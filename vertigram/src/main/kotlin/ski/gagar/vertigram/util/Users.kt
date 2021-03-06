package ski.gagar.vertigram.util

import ski.gagar.vertigram.entities.User

private const val UNKNOWN = "Кто-то"

private fun User.mdMention(text: String) = "[${text.escapeText()}](tg://user?id=${this.id})"

private val User.fullName: String?
    get() {
        val firstName = this.firstName ?: ""
        val lastName = this.lastName ?: ""
        val fullName = "$firstName $lastName".trim()
        return if (fullName.isBlank()) return null else fullName
    }

val User.mdMention
    get() = when {
        username != null -> "@$username".escapeText()
        fullName != null -> mdMention("$fullName")
        else -> mdMention(UNKNOWN)
    }

val User.softMdMention
    get() = when {
        username != null -> "`${"@$username".escapeCode()}`"
        fullName != null -> "`${fullName!!.escapeCode()}`"
        else -> "`${UNKNOWN.escapeCode()}`"
    }
