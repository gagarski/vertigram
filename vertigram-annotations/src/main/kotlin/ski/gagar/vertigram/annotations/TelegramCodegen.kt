package ski.gagar.vertigram.annotations

@Retention(AnnotationRetention.SOURCE)
annotation class TelegramCodegen() {
    /**
     * An annotation to mark classes representing Telegram methods.
     *
     * For the class `DoSomething` annotated with this annotation
     * `Telegram.doSomething(...)` extension function is generated.
     *
     * Used for code generation in [ski.gagar.vertigram.telegram.methods].
     * When annotating note that ksp supports default parameters in a very limited way
     * (and they are highly used in Telegram methods).
     *
     * To support default parameters the following assumptions are made:
     *  - default value for nullable parameter is null
     *  - default value for non-nullable boolean parameter is false
     *
     *  Sometimes these assumptions are wrong. For these cases special notion for default parameters using nested
     *  `Defaults` object is used. For a parameter X the field with name X from `Defaults` object will be used.
     *  See [ski.gagar.vertigram.telegram.types.methods.SendPoll] as an example. This won't allow you to use power
     *  of default values fully but still a reasonable trade-off to make methods generatable.
     *
     *  @see ski.gagar.vertigram.telegram.types.methods.TelegramCallable
     *  @see ski.gagar.vertigram.telegram.types.methods.EditChatInviteLink.WithJoinRequest
     *  @see ski.gagar.vertigram.telegram.types.methods.SendPoll
     */
    @Retention(AnnotationRetention.SOURCE)
    annotation class Method(
        /**
         * A name for generated method. By default, it's class name with lowercase first letter.
         */
        val name: String = "",
        /**
         * A name for the method as mentioned in Telegram docs, by default, [name] is used
         */
        val telegramName: String = "",
        /**
         * Wrap rich text parameters
         */
        val wrapRichText: Boolean = true
    )

    /**
     * An annotation to mark classes representing Telegram types.
     *
     * For the class `DoSomething` annotated with this annotation
     * `DoSomething.create(...)` creator function is generated.
     *
     * Used for code generation in [ski.gagar.vertigram.telegram.types].
     * When annotating note that ksp supports default parameters in a very limited way
     * (and they are highly used in Telegram methods).
     *
     * To support default parameters the following assumptions are made:
     *  - default value for nullable parameter is null
     *  - default value for non-nullable boolean parameter is false
     *
     *  Sometimes these assumptions are wrong. For these cases special notion for default parameters using nested
     *  `Defaults` object is used. For a parameter X the field with name X from `Defaults` object will be used.
     *  See [ski.gagar.vertigram.telegram.types.methods.SendPoll] as an example. This won't allow you to use power
     *  of default values fully but still a reasonable trade-off to make methods generatable.
     *
     *  @see ski.gagar.vertigram.telegram.types.methods.TelegramCallable
     *  @see ski.gagar.vertigram.telegram.types.methods.EditChatInviteLink.WithJoinRequest
     *  @see ski.gagar.vertigram.telegram.types.methods.SendPoll
     */
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type(
        /**
         * Generate wrapper getter for ...unwrapped params for rich text
         */
        val wrapRichText: Boolean = true
    )
}
