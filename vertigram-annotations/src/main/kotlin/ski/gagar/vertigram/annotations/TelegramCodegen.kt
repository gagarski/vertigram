package ski.gagar.vertigram.annotations

import java.lang.annotation.Inherited

/**
 * An annotation to mark classes representing Telegram methods.
 *
 * Used for code generation in [ski.gagar.vertigram.methods].
 * When annotating note that kapt supports default parameters in a very limited way
 * (and they are highly used in Telegram methods).
 *
 * To support default parameters the following assumptions are made:
 *  - default value for nullable parameter is null
 *  - default value for non-nullable boolean parameter is false
 *
 *  Sometimes these assumptions are wrong. For these cases special notion for default parameters using nested
 *  `Defaults` object is used. For a parameter X the field with name X from `Defaults` object will be used.
 *  See [ski.gagar.vertigram.methods.SendPoll] as an example. This won't allow you to use power
 *  of default values fully but still a reasonable trade-off to make methods generatable.
 *
 * This annotation is [Inherited]. To override annotation parameters in a subclass,
 * just add the annotation directly to it.
 *
 *  @see ski.gagar.vertigram.methods.TelegramCallable
 *  @see ski.gagar.vertigram.methods.EditChatInviteLink.WithJoinRequest
 *  @see ski.gagar.vertigram.methods.SendPoll
 */
@Inherited
@Retention(AnnotationRetention.SOURCE)
annotation class TelegramCodegen(
    /**
     * Should method (for `DoSomething` class `fun Telegram.doSomething(...)`) be generated?
     */
    val generateMethod: Boolean = true,
    /**
     * A name for generated method. By default, it's class name with lowercase first letter.
     */
    val methodName: String = "",
    /**
     * Should pseudo-constructor (for `DoSomething` class it's `fun DoSomething(...)`) be generated?
     *
     * Pseudo-constructors are useful only in some cases, when the desired signature of constructor
     * differs from the actual constructor which is defined by JSON structure:
     *  - Sub-classes not explicitly defined in Telegram documentation
     *    (see [ski.gagar.vertigram.methods.EditChatInviteLink.WithJoinRequest])
     *  - Rich-text parameters
     */
    val generatePseudoConstructor: Boolean = false,
    /**
     * A name for generated pseudo-constructor. By default, it's class name.
     */
    val pseudoConstructorName: String = ""
)
