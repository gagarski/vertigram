package ski.gagar.vertigram.util.internal

inline fun <reified D> Any?.uncheckedCast(): D = this as D

inline fun <reified D> Any?.uncheckedCastOrNull(): D? = when (this) {
    null -> null; else -> uncheckedCast()
}

