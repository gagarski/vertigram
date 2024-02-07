package ski.gagar.vertigram.annotations

/**
 * An annotation to mark classes representing Telegram methods.
 *
 * Used for code generation in [ski.gagar.vertigram.TgMethodsKt].
 * When annotating note that kapt supports default parameters in a very limited way
 * (and they are hightly used in telegram methods).
 *
 * To support default parameters the following assumptions are made:
 *  - default value for nullable parameter is null
 *  - default value for non-nullable boolean parameter is false
 *
 *  Sometimes these assumptions are wrong. For these cases special notion for default parameters using nested
 *  object is used. See [ski.gagar.vertigram.methods.SendPoll] as an example. This won't allow you to use power
 *  of default values fully but still a reasonable trade-off to make [ski.gagar.vertigram.TgMethodsKt]
 *  generatable.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class TgSuperClass()
