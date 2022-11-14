package ski.gagar.vxutil.vertigram.builders

import ski.gagar.vxutil.vertigram.types.CallbackGame
import ski.gagar.vxutil.vertigram.types.InlineKeyboardButton
import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.KeyboardButton
import ski.gagar.vxutil.vertigram.types.KeyboardButtonPollType
import ski.gagar.vxutil.vertigram.types.LoginUrl
import ski.gagar.vxutil.vertigram.types.ReplyKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.WebAppInfo

fun ikm(init: InlineKeyboardMarkupBuilder.() -> Unit): InlineKeyboardMarkup {
    val bld = InlineKeyboardMarkupBuilder()
    bld.init()
    return bld.build()
}

fun rkm(init: ReplyKeyboardMarkupBuilder.() -> Unit): ReplyKeyboardMarkup {
    val bld = ReplyKeyboardMarkupBuilder()
    bld.init()
    return bld.build()
}

@DslMarker
annotation class InlineKeyboardMarkupDslMarker

@DslMarker
annotation class ReplyKeyboardMarkupDslMarker

@InlineKeyboardMarkupDslMarker
class InlineKeyboardMarkupRowBuilder {
    private val buttons = mutableListOf<InlineKeyboardButton>()

    fun button(
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
            InlineKeyboardButton(
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

    fun build() = buttons
}

@InlineKeyboardMarkupDslMarker
class InlineKeyboardMarkupBuilder {
    private val rows = mutableListOf<List<InlineKeyboardButton>>()

    fun row(init: InlineKeyboardMarkupRowBuilder.() -> Unit) {
        val bld = InlineKeyboardMarkupRowBuilder()
        bld.init()
        rows.add(bld.build())
    }

    fun button(
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
        row {
            button(
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
        }
    }

    fun build() = InlineKeyboardMarkup(rows)
}

@ReplyKeyboardMarkupDslMarker
class ReplyKeyboardMarkupRowBuilder {
    private val buttons = mutableListOf<KeyboardButton>()

    fun button(
        text: String,
        requestContact: Boolean? = false,
        requestLocation: Boolean? = false,
        requestPoll: KeyboardButtonPollType? = null,
        webApp: WebAppInfo? = null
    ) {
        buttons.add(
            KeyboardButton(
                text = text,
                requestContact = requestContact,
                requestLocation =requestLocation,
                requestPoll = requestPoll,
                webApp = webApp
            )
        )
    }

    fun build() = buttons
}

@ReplyKeyboardMarkupDslMarker
class ReplyKeyboardMarkupBuilder {
    private val rows = mutableListOf<List<KeyboardButton>>()

    fun row(init: ReplyKeyboardMarkupRowBuilder.() -> Unit) {
        val bld = ReplyKeyboardMarkupRowBuilder()
        bld.init()
        rows.add(bld.build())
    }

    fun button(
        text: String,
        requestContact: Boolean? = false,
        requestLocation: Boolean? = false,
        requestPoll: KeyboardButtonPollType? = null,
        webApp: WebAppInfo? = null
    ) {
        row {
            button(
                text = text,
                requestContact = requestContact,
                requestLocation =requestLocation,
                requestPoll = requestPoll,
                webApp = webApp
            )
        }
    }

    fun build() = ReplyKeyboardMarkup(rows)
}
