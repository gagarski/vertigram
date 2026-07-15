package ski.gagar.vertigram.telegram.types

const val REDACTED_SENSITIVE_DATA = "<redacted>"

interface SensitiveData<out T> {
    fun copyWithoutSensitiveData(): T
}

interface SensitiveResult {
    fun withoutSensitiveData(result: Any?): Any?
}

internal fun Any?.copyWithoutSensitiveData() =
    (this as? SensitiveData<*>)?.copyWithoutSensitiveData() ?: this

internal fun Any?.withoutSensitiveResult(call: Any?) =
    (call as? SensitiveResult)?.withoutSensitiveData(this) ?: this
