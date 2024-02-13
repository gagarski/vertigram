package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ReplyMarkup](https://core.telegram.org/bots/api#replymarkup) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = ReplyMarkup.InlineKeyboard::class),
    JsonSubTypes.Type(value = ReplyMarkup.Keyboard::class),
    JsonSubTypes.Type(value = ReplyMarkup.KeyboardRemove::class),
    JsonSubTypes.Type(value = ReplyMarkup.ForceReply::class)
)
sealed interface ReplyMarkup {
    /**
     * Telegram [InlineKeyboardMarkup](https://core.telegram.org/bots/api#inlinekeyboardmarkup) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     *
     * Consider using [ski.gagar.vertigram.markup.keyboard] for building the button layout.
     *
     */
    data class InlineKeyboard(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inlineKeyboard: List<List<Button>>
    ) : ReplyMarkup {
        /**
         * Telegram [InlineKeyboardButton](https://core.telegram.org/bots/api#inlinekeyboardbutton) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Button(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val text: String,
            val url: String? = null,
            val callbackData: String? = null,
            val webApp: WebAppInfo? = null,
            val loginUrl: LoginUrl? = null,
            val switchInlineQuery: String? = null,
            val switchInlineQueryCurrentChat: String? = null,
            val switchInlineQueryChosenChat: SwitchInlineQueryChosenChat? = null,
            val callbackGame: CallbackGame? = null,
            val pay: Boolean = false
        ) {
            /**
             * Telegram [SwitchInlineQueryChosenChat](https://core.telegram.org/bots/api#switchinlinequerychosenchat) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data class SwitchInlineQueryChosenChat(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val query: String? = null,
                val allowUserChats: Boolean = false,
                val allowBotChats: Boolean = false,
                val allowGroupChats: Boolean = false,
                val allowChannelChats: Boolean = false
            )

        }
    }

    /**
     * Telegram [ReplyKeyboardMarkup](https://core.telegram.org/bots/api#replykeyboardmarkup) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     *
     * Consider using [ski.gagar.vertigram.markup.keyboard] for building the button layout.
     *
     */
    data class Keyboard(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val keyboard: List<List<Button>>,
        @get:JvmName("getIsPersistent")
        val isPersistent: Boolean = false,
        val resizeKeyboard: Boolean = false,
        val oneTimeKeyboard: Boolean = false,
        val inputFieldPlaceholder: String? = null,
        val selective: Boolean = false,
    ) : ReplyMarkup {
        /**
         * Telegram [KeyboardButton](https://core.telegram.org/bots/api#keyboardbutton) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = Button.Text::class)
        @JsonSubTypes(
            JsonSubTypes.Type(value = Button.Text::class),
            JsonSubTypes.Type(value = Button.RequestUsers::class),
            JsonSubTypes.Type(value = Button.RequestChat::class),
            JsonSubTypes.Type(value = Button.RequestContact::class),
            JsonSubTypes.Type(value = Button.RequestLocation::class),
            JsonSubTypes.Type(value = Button.RequestPoll::class),
            JsonSubTypes.Type(value = Button.WebApp::class),
        )
        sealed interface Button {
            data class Text(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String
            ) : Button

            /**
             * Case when [requestUsers] is specified
             */
            data class RequestUsers(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val requestUsers: Payload
            ) : Button {
                /**
                 * Telegram [KeyboardButtonRequestUsers](https://core.telegram.org/bots/api#keyboardbuttonrequestusers) type.
                 *
                 * For up-to-date documentation please consult the official Telegram docs.
                 */
                data class Payload(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    val requestId: Long,
                    val userIsBot: Boolean? = null,
                    val userIsPremium: Boolean? = null,
                    val maxQuantity: Long? = null
                )
            }

            /**
             * Case when [requestChat] is specified
             */
            data class RequestChat(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val requestChat: Payload
            ) : Button {
                /**
                 * Telegram [KeyboardButtonRequestChat](https://core.telegram.org/bots/api#keyboardbuttonrequestchat) type.
                 *
                 * For up-to-date documentation please consult the official Telegram docs.
                 */
                data class Payload(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    val requestId: Long,
                    val chatIsChannel: Boolean,
                    val chatIsForum: Boolean? = null,
                    val chatHasUsername: Boolean? = false,
                    val chatIsCreated: Boolean = false,
                    val userAdministratorRights: ChatAdministratorRights? = null,
                    val botAdministratorRights: ChatAdministratorRights? = null,
                    val botIsMember: Boolean = false
                )

            }

            /**
             * Case when [requestContact] is specified
             */
            data class RequestContact(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String
            ) : Button {
                val requestContact: Boolean = true
            }

            /**
             * Case when [requestLocation] is specified
             */
            data class RequestLocation(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String
            ) : Button {
                val requestLocation: Boolean = true
            }

            /**
             * Case when [requestPoll] is specified
             */
            data class RequestPoll(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val requestPoll: Payload
            ) : Button {
                /**
                 * Telegram [KeyboardButtonPollType](https://core.telegram.org/bots/api#keyboardbuttonpolltype) type.
                 *
                 * For up-to-date documentation please consult the official Telegram docs.
                 */
                data class Payload(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    val type: PollType? = null
                )
            }

            /**
             * Case when [requestLocation] is specified
             */
            data class WebApp(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val webApp: WebAppInfo
            ) : Button
        }
    }

    /**
     * Telegram [ReplyKeyboardRemove](https://core.telegram.org/bots/api#replykeyboardremove) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class KeyboardRemove(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val selective: Boolean = false
    ) : ReplyMarkup {
        @Suppress("unused")
        val removeKeyboard: Boolean = true
    }

    /**
     * Telegram [ForceReply](https://core.telegram.org/bots/api#forcereply) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class ForceReply(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inputFieldPlaceholder: String? = null,
        val selective: Boolean = false
    ) : ReplyMarkup {
        @Suppress("unused")
        val forceReply: Boolean = true
    }
}
