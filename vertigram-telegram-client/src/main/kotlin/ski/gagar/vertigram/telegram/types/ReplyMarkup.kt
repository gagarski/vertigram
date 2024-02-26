package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.util.NoPosArgs

/**
 * A common supertype for the following Telegram types:
 *  - [InlineKeyboardMarkup](https://core.telegram.org/bots/api#inlinekeyboardmarkup) — [ReplyMarkup.InlineKeyboard]
 *  - [ReplyKeyboardMarkup](https://core.telegram.org/bots/api#replykeyboardmarkup) — [ReplyMarkup.Keyboard]
 *  - [ReplyKeyboardRemove](https://core.telegram.org/bots/api#replykeyboardremove) — [ReplyMarkup.KeyboardRemove]
 *  - [ForceReply](https://core.telegram.org/bots/api#forcereply) — [ReplyMarkup.ForceReply]
 *
 *  This type is not explicitly present in Telegram docs, yet it's necessary where the typical "or"
 *  combination of the mentioned types is used.
 *
 * All the types, related to reply markup are nested (on one or few levels) inside this class.
 * More concise name is given where possible, given the fact that the class are nested to their related type.
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
     * The original type is split into subtypes given "You __must__ use exactly one of the optional fields."
     * condition, each of the subclasses represent the case of respecting field being set.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     *
     * Consider using [ski.gagar.vertigram.telegram.markup.inlineKeyboard] for building the button layout.
     */
    data class InlineKeyboard(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inlineKeyboard: List<List<Button>>
    ) : ReplyMarkup {
        /**
         * Telegram [InlineKeyboardButton](https://core.telegram.org/bots/api#inlinekeyboardbutton) type.
         *
         * The original type is split into subtypes given "You __must__ use exactly one of the optional fields."
         * condition, each of the subclasses represent the case of respecting field being set.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = Button.Text::class)
        @JsonSubTypes(
            JsonSubTypes.Type(value = Button.Text::class),
            JsonSubTypes.Type(value = Button.Url::class),
            JsonSubTypes.Type(value = Button.Callback::class),
            JsonSubTypes.Type(value = Button.WebApp::class),
            JsonSubTypes.Type(value = Button.Login::class),
            JsonSubTypes.Type(value = Button.SwitchInline::class),
            JsonSubTypes.Type(value = Button.SwitchInlineCurrentChat::class),
            JsonSubTypes.Type(value = Button.SwitchInlineChosenChat::class),
            JsonSubTypes.Type(value = Button.Game::class),
            JsonSubTypes.Type(value = Button.Pay::class),

        )
        sealed interface Button {
            /**
             * Case when no optional fields are specified
             */
            data class Text(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String
            ) : Button

            /**
             * Case when [url] is specified
             */
            data class Url(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val url: String
            ) : Button

            /**
             * Case when [callbackData] is specified
             */
            data class Callback(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val callbackData: String
            ) : Button

            /**
             * Case when [webApp] is specified
             */
            data class WebApp(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val webApp: WebAppInfo
            ) : Button

            /**
             * Case when [loginUrl] is specified
             */
            data class Login(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val loginUrl: Payload
            ) : Button {
                /**
                 * Telegram [LoginUrl](https://core.telegram.org/bots/api#loginurl) type.
                 *
                 * For up-to-date documentation please consult the official Telegram docs.
                 */
                data class Payload(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    val url: String,
                    val forwardText: String? = null,
                    val botUsername: String? = null,
                    val requestWriteAccess: Boolean = false
                )
            }

            /**
             * Case when [switchInlineQuery] is set
             */
            data class SwitchInline(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val switchInlineQuery: String,
            ) : Button

            /**
             * Case when [switchInlineQueryCurrentChat] is set
             */
            data class SwitchInlineCurrentChat(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val switchInlineQueryCurrentChat: String
            ) : Button

            /**
             * Case when [switchInlineQueryChosenChat] is set
             */
            data class SwitchInlineChosenChat(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val switchInlineQueryChosenChat: Payload
            ) : Button {
                /**
                 * Telegram [SwitchInlineQueryChosenChat](https://core.telegram.org/bots/api#switchinlinequerychosenchat) type.
                 *
                 * For up-to-date documentation please consult the official Telegram docs.
                 */
                data class Payload(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    val query: String? = null,
                    val allowUserChats: Boolean = false,
                    val allowBotChats: Boolean = false,
                    val allowGroupChats: Boolean = false,
                    val allowChannelChats: Boolean = false
                )
            }

            /**
             * Case when [callbackGame] is set
             */
            data class Game(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String,
                val callbackGame: Payload
            ) : Button {
                /**
                 * Telegram [CallbackGame](https://core.telegram.org/bots/api#callbackgame) type.
                 *
                 * For up-to-date documentation please consult the official Telegram docs.
                 */
                data object Payload
            }

            /**
             * Case when [pay] is set to `true`
             */
            data class Pay(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val text: String
            ) : Button {
                val pay: Boolean = true
            }
        }
    }


    /**
     * Telegram [ReplyKeyboardMarkup](https://core.telegram.org/bots/api#replykeyboardmarkup) type.
     *
     * The original type is split into subtypes given "You __must__ use exactly one of the optional fields."
     * condition, each of the subclasses represent the case of respecting field being set.
     *
     * Consider using [ski.gagar.vertigram.telegram.markup.keyboard] for building the button layout.
     *
     * For up-to-date documentation please consult the official Telegram docs.
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
            /**
             * Case when no optional fields are set
             */
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
                    val type: Poll.Type? = null
                )
            }

            /**
             * Case when [webApp] is specified
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
