package ski.gagar.vxutil

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <reified D> Any?.uncheckedCast(): D = this as D

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
inline fun <reified D> Any?.uncheckedCastOrNull(): D? = when (this) {
    null -> null; else -> uncheckedCast()
}

@Suppress("NOTHING_TO_INLINE", "UNUSED_PARAMETER")
inline fun ignore(u: Unit?) {
}

@Suppress("NOTHING_TO_INLINE", "UNUSED_PARAMETER")
inline fun <T> use(obj: T?) {
}
