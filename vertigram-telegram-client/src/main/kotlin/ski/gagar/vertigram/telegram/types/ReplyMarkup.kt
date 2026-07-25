package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
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
 * All reply-markup types are nested here and use concise names based on their enclosing type.
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
     * Represents an inline keyboard that appears next to its message.
     *
     * Consider using [ski.gagar.vertigram.telegram.markup.inlineKeyboard] to build the button layout.
     *
     * See Telegram's [InlineKeyboardMarkup](https://core.telegram.org/bots/api#inlinekeyboardmarkup) documentation.
     */
    data class InlineKeyboard internal constructor(
        /** Rows of inline keyboard buttons. */
        val inlineKeyboard: List<List<Button>>
    ) : ReplyMarkup {
        /**
         * Represents one button of an inline keyboard.
         *
         * Each subtype represents the single action performed by the button.
         *
         * See Telegram's [InlineKeyboardButton](https://core.telegram.org/bots/api#inlinekeyboardbutton) documentation.
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
            JsonSubTypes.Type(value = Button.CopyText::class),
            JsonSubTypes.Type(value = Button.Game::class),
            JsonSubTypes.Type(value = Button.Pay::class),

        )
        sealed interface Button {
            /** Case when the button has no action. */
            data class Text internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null
            ) : Button {
                companion object
            }

            /** Case when the button opens an HTTP or `tg://` URL. */
            data class Url internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** HTTP or `tg://` URL opened when the button is pressed. */
                val url: String
            ) : Button {
                companion object
            }

            /** Case when the button sends callback data to the bot. */
            data class Callback internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Data sent in a callback query, 1-64 bytes. */
                val callbackData: String
            ) : Button {
                companion object
            }

            /** Case when the button opens a Web App. */
            data class WebApp internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Web App opened when the button is pressed. */
                val webApp: WebAppInfo
            ) : Button {
                companion object
            }

            /** Case when the button performs automatic user authorization. */
            data class Login internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Login URL configuration. */
                val loginUrl: Payload
            ) : Button {
                /**
                 * Represents a parameter of an inline keyboard button used for automatic user authorization.
                 *
                 * See Telegram's [LoginUrl](https://core.telegram.org/bots/api#loginurl) documentation.
                 */
                data class Payload(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    /** HTTPS URL to be opened with user authorization data. */
                    val url: String,
                    /** New text of the button in forwarded messages. */
                    val forwardText: String? = null,
                    /** Username of a bot used for user authorization. */
                    val botUsername: String? = null,
                    /** Whether the bot should request permission to send messages to the user. */
                    val requestWriteAccess: Boolean = false
                )
                companion object
            }

            /** Case when the button prompts the user to select a chat and insert the bot's username and query. */
            data class SwitchInline internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Inline query inserted into the selected chat. */
                val switchInlineQuery: String,
            ) : Button {
                companion object
            }

            /** Case when the button inserts the bot's username and query in the current chat. */
            data class SwitchInlineCurrentChat internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Inline query inserted into the current chat. */
                val switchInlineQueryCurrentChat: String
            ) : Button {
                companion object
            }

            /** Case when the button prompts the user to select an allowed chat for an inline query. */
            data class SwitchInlineChosenChat internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Chat-selection and query configuration. */
                val switchInlineQueryChosenChat: Payload
            ) : Button {
                /**
                 * Represents a button action that prompts the user to select a chat for an inline query.
                 *
                 * See Telegram's
                 * [SwitchInlineQueryChosenChat](https://core.telegram.org/bots/api#switchinlinequerychosenchat)
                 * documentation.
                 */
                data class Payload internal constructor(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    /** Inline query inserted in the selected chat. */
                    val query: String? = null,
                    /** Whether private chats with users can be selected. */
                    val allowUserChats: Boolean = false,
                    /** Whether private chats with bots can be selected. */
                    val allowBotChats: Boolean = false,
                    /** Whether group and supergroup chats can be selected. */
                    val allowGroupChats: Boolean = false,
                    /** Whether channel chats can be selected. */
                    val allowChannelChats: Boolean = false
                ) {
                    companion object
                }

                companion object
            }

            /** Case when the button copies text to the clipboard. */
            data class CopyText internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Text-copy configuration. */
                val copyText: Payload
            ) : Button {
                /**
                 * Represents an inline keyboard button that copies text to the clipboard.
                 *
                 * See Telegram's [CopyTextButton](https://core.telegram.org/bots/api#copytextbutton) documentation.
                 */
                data class Payload internal constructor(
                    /** Text to copy, 1-256 characters. */
                    val text: String
                ) {
                    companion object
                }

                companion object
            }


            /** Case when the button launches a game. */
            data class Game internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Game-launching payload. */
                val callbackGame: Payload
            ) : Button {
                /**
                 * Placeholder for a callback game.
                 *
                 * See Telegram's [CallbackGame](https://core.telegram.org/bots/api#callbackgame) documentation.
                 */
                data object Payload

                companion object
            }

            /** Case when the button is a payment button. */
            data class Pay internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Label text on the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null
            ) : Button {
                val pay: Boolean = true

                companion object
            }
        }

        companion object
    }


    /**
     * Represents a custom keyboard with reply options.
     *
     * Consider using [ski.gagar.vertigram.telegram.markup.keyboard] to build the button layout.
     *
     * See Telegram's [ReplyKeyboardMarkup](https://core.telegram.org/bots/api#replykeyboardmarkup) documentation.
     */
    data class Keyboard internal constructor(
        /** Rows of keyboard buttons. */
        val keyboard: List<List<Button>>,
        /** Whether to always show the keyboard when the regular keyboard is hidden. */
        @get:JvmName("getIsPersistent")
        val isPersistent: Boolean = false,
        /** Whether clients should resize the keyboard vertically for an optimal fit. */
        val resizeKeyboard: Boolean = false,
        /** Whether clients should hide the keyboard after it is used. */
        val oneTimeKeyboard: Boolean = false,
        /** Placeholder shown in the input field while the keyboard is active. */
        val inputFieldPlaceholder: String? = null,
        /** Whether the keyboard is shown only to specific users. */
        val selective: Boolean = false,
    ) : ReplyMarkup {
        /**
         * Represents one button of a reply keyboard.
         *
         * Each subtype represents the single action performed by the button.
         *
         * See Telegram's [KeyboardButton](https://core.telegram.org/bots/api#keyboardbutton) documentation.
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = Button.Text::class)
        @JsonSubTypes(
            JsonSubTypes.Type(value = Button.Text::class),
            JsonSubTypes.Type(value = Button.RequestUsers::class),
            JsonSubTypes.Type(value = Button.RequestChat::class),
            JsonSubTypes.Type(value = Button.RequestManagedBot::class),
            JsonSubTypes.Type(value = Button.RequestContact::class),
            JsonSubTypes.Type(value = Button.RequestLocation::class),
            JsonSubTypes.Type(value = Button.RequestPoll::class),
            JsonSubTypes.Type(value = Button.WebApp::class),
        )
        sealed interface Button {
            /** Case when the button sends its text as a message. */
            data class Text internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null
            ) : Button {
                companion object
            }

            /** Case when the button requests that the user select and share users with the bot. */
            data class RequestUsers internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** User-selection criteria. */
                val requestUsers: Payload
            ) : Button {
                /**
                 * Defines the criteria used to request suitable users.
                 *
                 * See Telegram's
                 * [KeyboardButtonRequestUsers](https://core.telegram.org/bots/api#keyboardbuttonrequestusers)
                 * documentation.
                 */
                data class Payload internal constructor(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    /** Identifier of the request. */
                    val requestId: Long,
                    /** Required bot status of selected users. */
                    val userIsBot: Boolean? = null,
                    /** Required premium status of selected users. */
                    val userIsPremium: Boolean? = null,
                    /** Maximum number of users that can be selected; 1-10. */
                    val maxQuantity: Long? = null,
                    /** Whether to request the users' first and last names. */
                    val requestName: Boolean = false,
                    /** Whether to request the users' usernames. */
                    val requestUsername: Boolean = false,
                    /** Whether to request the users' photos. */
                    val requestPhoto: Boolean = false
                ) {
                    companion object
                }
                companion object
            }

            /** Case when the button requests that the user select and share a chat with the bot. */
            data class RequestChat internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Chat-selection criteria. */
                val requestChat: Payload
            ) : Button {
                /**
                 * Defines the criteria used to request a suitable chat.
                 *
                 * See Telegram's
                 * [KeyboardButtonRequestChat](https://core.telegram.org/bots/api#keyboardbuttonrequestchat)
                 * documentation.
                 */
                data class Payload internal constructor(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    /** Identifier of the request. */
                    val requestId: Long,
                    /** Whether the requested chat must be a channel. */
                    val chatIsChannel: Boolean,
                    /** Required forum status of the requested chat. */
                    val chatIsForum: Boolean? = null,
                    /** Required username status of the requested chat. */
                    val chatHasUsername: Boolean? = false,
                    /** Whether the requested chat must have been created by the user. */
                    val chatIsCreated: Boolean = false,
                    /** Required administrator rights of the user in the requested chat. */
                    val userAdministratorRights: ChatAdministratorRights? = null,
                    /** Required administrator rights of the bot in the requested chat. */
                    val botAdministratorRights: ChatAdministratorRights? = null,
                    /** Whether the bot must already be a member of the requested chat. */
                    val botIsMember: Boolean = false,
                    /** Whether to request the chat title. */
                    val requestTitle: Boolean = false,
                    /** Whether to request the chat username. */
                    val requestUsername: Boolean = false,
                    /** Whether to request the chat photo. */
                    val requestPhoto: Boolean = false
                ) {
                    companion object
                }
                companion object
            }

            /** Case when the button requests creation of a managed bot. */
            data class RequestManagedBot internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Managed-bot creation parameters. */
                val requestManagedBot: Payload
            ) : Button {
                /**
                 * Defines the parameters used to request creation of a managed bot.
                 *
                 * See Telegram's
                 * [KeyboardButtonRequestManagedBot](https://core.telegram.org/bots/api#keyboardbuttonrequestmanagedbot)
                 * documentation.
                 */
                data class Payload internal constructor(
                    @JsonIgnore
                    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    /** Identifier of the request. */
                    val requestId: Long,
                    /** Suggested first name for the managed bot. */
                    val suggestedName: String? = null,
                    /** Suggested username for the managed bot. */
                    val suggestedUsername: String? = null
                ) {
                    companion object
                }
                companion object
            }

            /** Case when the button requests the user's phone number. */
            data class RequestContact internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null
            ) : Button {
                val requestContact: Boolean = true

                companion object
            }

            /** Case when the button requests the user's current location. */
            data class RequestLocation internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null
            ) : Button {
                val requestLocation: Boolean = true

                companion object
            }

            /** Case when the button requests that the user create and send a poll. */
            data class RequestPoll internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Poll-type restriction. */
                val requestPoll: Payload
            ) : Button {
                /**
                 * Represents a poll type allowed to be created from a keyboard button.
                 *
                 * See Telegram's
                 * [KeyboardButtonPollType](https://core.telegram.org/bots/api#keyboardbuttonpolltype) documentation.
                 */
                data class Payload internal constructor(
                    /** Type of poll that can be created. */
                    val type: Poll.Type? = null
                ) {
                    companion object
                }

                companion object
            }

            /** Case when the button opens a Web App. */
            data class WebApp internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                /** Text of the button. */
                val text: String,
                /** Unique identifier of a custom emoji shown before [text]. */
                val iconCustomEmojiId: String? = null,
                /** Style of the button. */
                val style: ButtonStyle? = null,
                /** Web App opened when the button is pressed. */
                val webApp: WebAppInfo
            ) : Button {
                companion object
            }
        }

        companion object
    }

    /**
     * Tells clients to remove the current custom keyboard.
     *
     * See Telegram's [ReplyKeyboardRemove](https://core.telegram.org/bots/api#replykeyboardremove) documentation.
     */
    data class KeyboardRemove internal constructor(
        /** Whether to remove the keyboard only for specific users. */
        val selective: Boolean = false
    ) : ReplyMarkup {
        @Suppress("unused")
        val removeKeyboard: Boolean = true

        companion object
    }

    /**
     * Tells clients to display a reply interface to the user.
     *
     * See Telegram's [ForceReply](https://core.telegram.org/bots/api#forcereply) documentation.
     */
    data class ForceReply internal constructor(
        /** Placeholder shown in the input field when the reply is active. */
        val inputFieldPlaceholder: String? = null,
        /** Whether to force a reply only from specific users. */
        val selective: Boolean = false
    ) : ReplyMarkup {
        @Suppress("unused")
        val forceReply: Boolean = true

        companion object
    }

    enum class ButtonStyle {
        /** Destructive-action button style. */
        @JsonProperty("danger")
        DANGER,
        /** Successful-action button style. */
        @JsonProperty("success")
        SUCCESS,
        /** Primary-action button style. */
        @JsonProperty("primary")
        PRIMARY
    }

    companion object
}
