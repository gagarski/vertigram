package ski.gagar.vertigram.markup

import ski.gagar.vertigram.types.CallbackGame
import ski.gagar.vertigram.types.ChatAdministratorRights
import ski.gagar.vertigram.types.LoginUrl
import ski.gagar.vertigram.types.PollType
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.WebAppInfo
import ski.gagar.vertigram.util.NoPosArgs

/**
 * A builder for [ReplyMarkup.InlineKeyboard].
 *
 * @sample inlineKeyboardSample
 */
fun inlineKeyboard(init: InlineKeyboardMarkupBuilder.() -> Unit): ReplyMarkup.InlineKeyboard {
    val bld = InlineKeyboardMarkupBuilder()
    bld.init()
    return bld.build()
}

/**
 * A builder for [ReplyMarkup.Keyboard].
 *
 * Payloads for button types are unwrapped.
 *
 * @sample keyboardSample
 */
fun keyboard(
    @Suppress("UNUSED_PARAMETER")
    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    isPersistent: Boolean = false,
    resizeKeyboard: Boolean = false,
    oneTimeKeyboard: Boolean = false,
    inputFieldPlaceholder: String? = null,
    selective: Boolean = false,
    init: ReplyKeyboardMarkupBuilder.() -> Unit
): ReplyMarkup.Keyboard {
    val bld = ReplyKeyboardMarkupBuilder()
    bld.init()
    return bld.build(
        isPersistent = isPersistent,
        resizeKeyboard = resizeKeyboard,
        oneTimeKeyboard = oneTimeKeyboard,
        inputFieldPlaceholder = inputFieldPlaceholder,
        selective = selective
    )
}

/**
 * A builder for [ReplyMarkup.KeyboardRemove]
 */
fun keyboardRemove(selective: Boolean) = ReplyMarkup.KeyboardRemove(selective = selective)

/**
 * A builder for [ReplyMarkup.ForceReply]
 */
fun forceReply(inputFieldPlaceholder: String? = null,
               selective: Boolean) =
    ReplyMarkup.ForceReply(
        inputFieldPlaceholder = inputFieldPlaceholder,
        selective = selective
    )

@DslMarker
private annotation class InlineKeyboardMarkupDslMarker

@DslMarker
private annotation class ReplyKeyboardMarkupDslMarker

/**
 * Builder implementation for [InlineKeyboardMarkupBuilder.row]
 */
@InlineKeyboardMarkupDslMarker
class InlineKeyboardMarkupRowBuilder {
    private val buttons = mutableListOf<ReplyMarkup.InlineKeyboard.Button>()

    /**
     * Add [ReplyMarkup.InlineKeyboard.Button] to current row
     */
    fun button(
        @Suppress("UNUSED_PARAMETER")
        noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        text: String,
        url: String? = null,
        loginUrl: LoginUrl? = null,
        callbackData: String? = null,
        webApp: WebAppInfo? = null,
        switchInlineQuery: String? = null,
        switchInlineQueryCurrentChat: String? = null,
        callbackGame: CallbackGame? = null,
        pay: Boolean = false
    ) {
        buttons.add(
            ReplyMarkup.InlineKeyboard.Button(
                text = text,
                url = url,
                loginUrl = loginUrl,
                callbackData = callbackData,
                webApp = webApp,
                switchInlineQuery = switchInlineQuery,
                switchInlineQueryCurrentChat = switchInlineQueryCurrentChat,
                callbackGame = callbackGame,
                pay = pay
            )
        )
    }

    /**
     * @return resulting row
     */
    fun build() = buttons
}

/**
 * Builder implementation for [inlineKeyboard]
 */
@InlineKeyboardMarkupDslMarker
class InlineKeyboardMarkupBuilder {
    private val rows = mutableListOf<List<ReplyMarkup.InlineKeyboard.Button>>()

    /**
     * Add a row of buttons built by [init]
     *
     * @param init code around row builder
     */
    fun row(init: InlineKeyboardMarkupRowBuilder.() -> Unit) {
        val bld = InlineKeyboardMarkupRowBuilder()
        bld.init()
        rows.add(bld.build())
    }

    /**
     * @return resulting [ReplyMarkup.InlineKeyboard]
     */
    fun build() = ReplyMarkup.InlineKeyboard(inlineKeyboard = rows)
}

/**
 * Builder implementation for [ReplyKeyboardMarkupBuilder.row]
 */
@ReplyKeyboardMarkupDslMarker
class ReplyKeyboardMarkupRowBuilder {
    private val buttons = mutableListOf<ReplyMarkup.Keyboard.Button>()

    /**
     * Add simple text button
     *
     * @see [ReplyMarkup.Keyboard.Button.Text]
     */
    fun text(text: String) {
        buttons.add(ReplyMarkup.Keyboard.Button.Text(text = text))
    }

    /**
     * Add a button that requests users.
     *
     * @see [ReplyMarkup.Keyboard.Button.RequestUsers]
     */
    fun requestUsers(
        @Suppress("UNUSED_PARAMETER")
        noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        text: String,
        requestId: Long,
        userIsBot: Boolean? = null,
        userIsPremium: Boolean? = null,
        maxQuantity: Long? = null
    ) {
        buttons.add(
            ReplyMarkup.Keyboard.Button.RequestUsers(
                text = text,
                requestUsers = ReplyMarkup.Keyboard.Button.RequestUsers.Payload(
                    requestId = requestId,
                    userIsBot = userIsBot,
                    userIsPremium = userIsPremium,
                    maxQuantity = maxQuantity
                )
            )
        )
    }

    /**
     * Add a button that requests chat.
     *
     * @see [ReplyMarkup.Keyboard.Button.RequestChat]
     */
    fun requestChat(
        @Suppress("UNUSED_PARAMETER")
        noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        text: String,
        requestId: Long,
        chatIsChannel: Boolean,
        chatIsForum: Boolean? = null,
        chatHasUsername: Boolean? = false,
        chatIsCreated: Boolean = false,
        userAdministratorRights: ChatAdministratorRights? = null,
        botAdministratorRights: ChatAdministratorRights? = null,
        botIsMember: Boolean = false
    ) {
        buttons.add(
            ReplyMarkup.Keyboard.Button.RequestChat(
                text = text,
                requestChat = ReplyMarkup.Keyboard.Button.RequestChat.Payload(
                    requestId = requestId,
                    chatIsChannel = chatIsChannel,
                    chatIsForum = chatIsForum,
                    chatHasUsername = chatHasUsername,
                    chatIsCreated = chatIsCreated,
                    userAdministratorRights = userAdministratorRights,
                    botAdministratorRights = botAdministratorRights,
                    botIsMember = botIsMember
                )
            )
        )
    }

    /**
     * Add a button that requests contact.
     *
     * @see [ReplyMarkup.Keyboard.Button.RequestContact]
     */
    fun requestContact(text: String) {
        buttons.add(ReplyMarkup.Keyboard.Button.RequestContact(text = text))
    }

    /**
     * Add a button that requests location.
     *
     * @see [ReplyMarkup.Keyboard.Button.RequestLocation]
     */
    fun requestLocation(text: String) {
        buttons.add(ReplyMarkup.Keyboard.Button.RequestLocation(text = text))
    }

    /**
     * Add a button that requests a poll.
     *
     * @see [ReplyMarkup.Keyboard.Button.RequestPoll]
     */
    fun requestPoll(text: String, pollType: PollType) {
        buttons.add(
            ReplyMarkup.Keyboard.Button.RequestPoll(
                text = text,
                requestPoll = ReplyMarkup.Keyboard.Button.RequestPoll.Payload(
                    type = pollType
                )
            )
        )
    }

    /**
     * Add a button that requests a regular poll.
     *
     * @see [ReplyMarkup.Keyboard.Button.RequestPoll]
     */
    fun requestPoll(text: String) {
        requestPoll(text, PollType.REGULAR)
    }

    /**
     * Add a button that a quiz.
     *
     * @see [ReplyMarkup.Keyboard.Button.RequestPoll]
     */
    fun requestQuiz(text: String) {
        requestPoll(text, PollType.QUIZ)
    }

    /**
     * Add a web app button.
     *
     * @see [ReplyMarkup.Keyboard.Button.WebApp]
     */
    fun webApp(text: String, webApp: WebAppInfo) {
        buttons.add(
            ReplyMarkup.Keyboard.Button.WebApp(
                text = text,
                webApp = webApp
            )
        )
    }

    /**
     * @return resulting row
     */
    fun build() = buttons
}

/**
 * Builder implementation for [keyboard]
 */
@ReplyKeyboardMarkupDslMarker
class ReplyKeyboardMarkupBuilder {
    private val rows = mutableListOf<List<ReplyMarkup.Keyboard.Button>>()

    /**
     * Add a row of buttons built by [init]
     *
     * @param init code around row builder
     */
    fun row(init: ReplyKeyboardMarkupRowBuilder.() -> Unit) {
        val bld = ReplyKeyboardMarkupRowBuilder()
        bld.init()
        rows.add(bld.build())
    }
    /**
     * @return resulting [ReplyMarkup.Keyboard]
     */
    fun build(
        @Suppress("UNUSED_PARAMETER")
        noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        isPersistent: Boolean = false,
        resizeKeyboard: Boolean = false,
        oneTimeKeyboard: Boolean = false,
        inputFieldPlaceholder: String? = null,
        selective: Boolean = false,
    ) = ReplyMarkup.Keyboard(
        keyboard = rows,
        isPersistent = isPersistent,
        resizeKeyboard = resizeKeyboard,
        oneTimeKeyboard = oneTimeKeyboard,
        inputFieldPlaceholder = inputFieldPlaceholder,
        selective = selective
    )
}

/**
 * A sample for [inlineKeyboard] for documentation purposes
 */
private fun inlineKeyboardSample() {
    inlineKeyboard {
        row {
            button(text = "Button 1")
            button(text = "Button 2", url = "https://example.com")
        }

        row {
            button(text = "Button 3")
            button(text = "Button 4", callbackData = "callback")
        }
    }
}

/**
 * A sample for [keyboard] for documentation purposes
 */
private fun keyboardSample() {
    keyboard {
        row {
            text(text = "Simple text")
        }
        row {
            requestUsers(text = "Request users", requestId = 1)
            requestLocation("Request location")
        }
    }
}