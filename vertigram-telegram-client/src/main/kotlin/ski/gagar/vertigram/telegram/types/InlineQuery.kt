package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedCaption
import ski.gagar.vertigram.telegram.types.formattedtext.HasFormattedText
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.jackson.typing.TypeResolverWithDeductionBuilder
import java.time.Duration

/**
 * Represents an incoming inline query. When the user sends an empty query, the bot could return some default or
 * trending results.
 *
 * See Telegram's [InlineQuery](https://core.telegram.org/bots/api#inlinequery) documentation.
 */
@TelegramCodegen.Type
data class InlineQuery internal constructor(
    /** Unique identifier for this query. */
    val id: String,
    /** Sender. */
    val from: User,
    /** Text of the query, up to 256 characters. */
    val query: String,
    /** Offset of the results to be returned, controlled by the bot. */
    val offset: String,
    /**
     * Type of the chat from which the inline query was sent. The chat type should always be known for requests from
     * official clients and most third-party clients, unless the request was sent from a secret chat.
     */
    val chatType: Chat.Type? = null,
    /** Sender location, for bots that request user location. */
    val location: Location? = null
) {

    /**
     * Represents the content of a message to be sent as a result of an inline query.
     *
     * Telegram's `InputXxxMessageContent` types are represented as nested `InputMessageContent.Xxx` types.
     *
     * See Telegram's [InputMessageContent](https://core.telegram.org/bots/api#inputmessagecontent) documentation.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION,
        defaultImpl = InputMessageContent.Location::class // Looks VERY flaky, see https://github.com/FasterXML/jackson-databind/issues/2976
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = InputMessageContent.Text::class),
        JsonSubTypes.Type(value = InputMessageContent.Location::class),
        JsonSubTypes.Type(value = InputMessageContent.Venue::class),
        JsonSubTypes.Type(value = InputMessageContent.Contact::class),
        JsonSubTypes.Type(value = InputMessageContent.Invoice::class),
        JsonSubTypes.Type(value = InputMessageContent.Rich::class),
    )
    sealed interface InputMessageContent {
        /**
         * Case when the content is a text message to be sent as the result of an inline query.
         *
         * See Telegram's [InputTextMessageContent](https://core.telegram.org/bots/api#inputtextmessagecontent)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Text internal constructor(
            /** Text of the message to be sent, 1-4096 characters. */
            val messageText: String,
            /** Mode for parsing entities in the message text. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in the message text, specified instead of [parseMode]. */
            override val entities: List<MessageEntity>? = null,
            /** Link preview generation options for the message. */
            val linkPreviewOptions: Message.LinkPreviewOptions? = null
        ) : InputMessageContent, HasFormattedText {
            @JsonIgnore
            override val text = messageText

            companion object
        }

        /**
         * Case when the content is a rich message to be sent as the result of an inline query.
         *
         * See Telegram's [InputRichMessageContent](https://core.telegram.org/bots/api#inputrichmessagecontent)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Rich internal constructor(
            /** Message to be sent. */
            val richMessage: InputRichMessage
        ) : InputMessageContent {
            companion object
        }

        /**
         * Case when the content is a location message to be sent as the result of an inline query.
         *
         * See Telegram's [InputLocationMessageContent](https://core.telegram.org/bots/api#inputlocationmessagecontent)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Location internal constructor(
            /** Latitude of the location in degrees. */
            val latitude: Double,
            /** Longitude of the location in degrees. */
            val longitude: Double,
            /** Radius of uncertainty for the location in meters; 0-1500. */
            val horizontalAccuracy: Double? = null,
            /**
             * Period during which the location can be updated; 60-86400 seconds, or `0x7FFFFFFF` for a live location
             * that can be edited indefinitely.
             */
            val livePeriod: Duration? = null,
            /** For live locations, direction in which the user is moving, in degrees; 1-360. */
            val heading: Int? = null,
            /** For live locations, maximum distance for proximity alerts, in meters; 1-100000. */
            val proximityAlertRadius: Int? = null,
        ) : InputMessageContent {
            companion object
        }

        /**
         * Case when the content is a venue message to be sent as the result of an inline query.
         *
         * See Telegram's [InputVenueMessageContent](https://core.telegram.org/bots/api#inputvenuemessagecontent)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Venue internal constructor(
            /** Latitude of the venue in degrees. */
            val latitude: Double,
            /** Longitude of the venue in degrees. */
            val longitude: Double,
            /** Name of the venue. */
            val title: String,
            /** Address of the venue. */
            val address: String,
            /** Foursquare identifier of the venue, if known. */
            val foursquareId: String? = null,
            /** Foursquare type of the venue, if known. */
            val foursquareType: String? = null,
            /** Google Places identifier of the venue. */
            val googlePlaceId: String? = null,
            /** Google Places type of the venue. */
            val googlePlaceType: String? = null
        ) : InputMessageContent {
            companion object
        }

        /**
         * Case when the content is a contact message to be sent as the result of an inline query.
         *
         * See Telegram's [InputContactMessageContent](https://core.telegram.org/bots/api#inputcontactmessagecontent)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Contact internal constructor(
            /** Contact's phone number. */
            val phoneNumber: String,
            /** Contact's first name. */
            val firstName: String,
            /** Contact's last name. */
            val lastName: String? = null,
            /** Additional data about the contact in the form of a vCard, 0-2048 bytes. */
            val vcard: String? = null
        ) : InputMessageContent {
            companion object
        }

        /**
         * Case when the content is an invoice message to be sent as the result of an inline query.
         *
         * See Telegram's [InputInvoiceMessageContent](https://core.telegram.org/bots/api#inputinvoicemessagecontent)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Invoice internal constructor(
            /** Product name, 1-32 characters. */
            val title: String,
            /** Product description, 1-255 characters. */
            val description: String,
            /** Bot-defined invoice payload, 1-128 bytes. Not displayed to the user. */
            val payload: String,
            /**
             * Payment provider token obtained through [@BotFather](https://t.me/BotFather). Use an empty string for
             * payments in [Telegram Stars](https://t.me/BotNews/90).
             */
            val providerToken: String? = null,
            /**
             * Three-letter ISO 4217 currency code. Use `XTR` for payments in
             * [Telegram Stars](https://t.me/BotNews/90).
             */
            val currency: String,
            /**
             * Price breakdown. Must contain exactly one item for payments in
             * [Telegram Stars](https://t.me/BotNews/90).
             */
            val prices: List<LabeledPrice>,
            /**
             * Maximum accepted tip in the smallest units of [currency]. Not supported for payments in
             * [Telegram Stars](https://t.me/BotNews/90).
             */
            val maxTipAmount: Int? = null,
            /**
             * Suggested tip amounts in the smallest units of [currency]. At most four positive amounts can be
             * specified, in strictly increasing order, without exceeding [maxTipAmount].
             */
            val suggestedTipAmounts: List<Int>? = null,
            /** Data about the invoice shared with the payment provider. */
            val providerData: String? = null,
            /** URL of the product photo for the invoice. */
            val photoUrl: String? = null,
            /** Photo size in bytes. */
            val photoSize: Long? = null,
            /** Photo width. */
            val photoWidth: Int? = null,
            /** Photo height. */
            val photoHeight: Int? = null,
            /** Whether the user's full name is required to complete the order. Ignored for Telegram Stars. */
            val needName: Boolean = false,
            /** Whether the user's phone number is required to complete the order. Ignored for Telegram Stars. */
            val needPhoneNumber: Boolean = false,
            /** Whether the user's email address is required to complete the order. Ignored for Telegram Stars. */
            val needEmail: Boolean = false,
            /** Whether the user's shipping address is required to complete the order. Ignored for Telegram Stars. */
            val needShippingAddress: Boolean = false,
            /** Whether the user's phone number should be sent to the provider. Ignored for Telegram Stars. */
            val sendPhoneNumberToProvider: Boolean = false,
            /** Whether the user's email address should be sent to the provider. Ignored for Telegram Stars. */
            val sendEmailToProvider: Boolean = false,
            /** Whether the final price depends on the shipping method. Ignored for Telegram Stars. */
            @get:JvmName("getIsFlexible")
            val isFlexible: Boolean = false
        ) : InputMessageContent, SensitiveData<Invoice> {
            override fun copyWithoutSensitiveData() =
                copy(providerToken = providerToken?.let { REDACTED_SENSITIVE_DATA })

            companion object
        }
    }


    /**
     * Represents one result of an inline query.
     *
     * Telegram's `InlineQueryResultXxx` types are represented as `Result.Xxx`, and
     * `InlineQueryResultCachedXxx` types as `Result.Xxx.Cached`. All URLs passed in inline query results are available
     * to end users and must be assumed to be public.
     *
     * See Telegram's [InlineQueryResult](https://core.telegram.org/bots/api#inlinequeryresult) documentation.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Result.Article::class, name = Result.Type.ARTICLE_STR),
        JsonSubTypes.Type(value = Result.Audio::class, name = Result.Type.AUDIO_STR),
        JsonSubTypes.Type(value = Result.Audio.Cached::class, name = Result.Type.AUDIO_STR),
        JsonSubTypes.Type(value = Result.Contact::class, name = Result.Type.CONTACT_STR),
        JsonSubTypes.Type(value = Result.Document::class, name = Result.Type.DOCUMENT_STR),
        JsonSubTypes.Type(value = Result.Document.Cached::class, name = Result.Type.DOCUMENT_STR),
        JsonSubTypes.Type(value = Result.Game::class, name = Result.Type.GAME_STR),
        JsonSubTypes.Type(value = Result.Gif::class, name = Result.Type.GIF_STR),
        JsonSubTypes.Type(value = Result.Gif.Cached::class, name = Result.Type.GIF_STR),
        JsonSubTypes.Type(value = Result.Location::class, name = Result.Type.LOCATION_STR),
        JsonSubTypes.Type(value = Result.Mpeg4Gif::class, name = Result.Type.MPEG4_GIF_STR),
        JsonSubTypes.Type(value = Result.Mpeg4Gif.Cached::class, name = Result.Type.MPEG4_GIF_STR),
        JsonSubTypes.Type(value = Result.Photo::class, name = Result.Type.PHOTO_STR),
        JsonSubTypes.Type(value = Result.Photo.Cached::class, name = Result.Type.PHOTO_STR),
        // Omitted intentionally there is no such class on telegram
        // JsonSubTypes.Type(value = Result.Sticker::class, name = Result.Type.STICKER_STR),
        JsonSubTypes.Type(value = Result.Sticker.Cached::class, name = Result.Type.STICKER_STR),
        JsonSubTypes.Type(value = Result.Venue::class, name = Result.Type.VENUE_STR),
        JsonSubTypes.Type(value = Result.Video::class, name = Result.Type.VIDEO_STR),
        JsonSubTypes.Type(value = Result.Video.Cached::class, name = Result.Type.VIDEO_STR),
        JsonSubTypes.Type(value = Result.Voice::class, name = Result.Type.VOICE_STR),
        JsonSubTypes.Type(value = Result.Voice.Cached::class, name = Result.Type.VOICE_STR)
    )
    @JsonTypeResolver(TypeResolverWithDeductionBuilder::class)
    sealed interface Result {
        val type: Type
        val id: String

        /**
         * Represents a button to be shown above inline query results. Exactly one of [webApp] and [startParameter]
         * must be used.
         *
         * This class is not a subclass of [Result].
         *
         * See Telegram's [InlineQueryResultsButton](https://core.telegram.org/bots/api#inlinequeryresultsbutton)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Button internal constructor(
            /** Label text on the button. */
            val text: String,
            /** Web App launched when the user presses the button. */
            val webApp: WebAppInfo? = null,
            /**
             * Deep-linking parameter for the `/start` message sent to the bot when the user presses the button.
             * Must be 1-64 characters and contain only `A-Z`, `a-z`, `0-9`, `_`, and `-`.
             */
            val startParameter: String? = null
        ) {
            companion object
        }

        /**
         * Case when the result represents a link to an article or web page.
         *
         * See Telegram's [InlineQueryResultArticle](https://core.telegram.org/bots/api#inlinequeryresultarticle)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Article internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Title of the result. */
            val title: String,
            /** Content of the message to be sent. */
            val inputMessageContent: InputMessageContent,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** URL of the result. */
            val url: String? = null,
            /** Short description of the result. */
            val description: String? = null,
            /** URL of the thumbnail for the result. */
            val thumbnailUrl: String? = null,
            /** Thumbnail width. */
            val thumbnailWidth: Int? = null,
            /** Thumbnail height. */
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type = Type.ARTICLE

            companion object;
        }

        /**
         * Case when the result represents a link to an MP3 audio file.
         *
         * By default, the audio file is sent by the user. [inputMessageContent] can be used to send the specified
         * content instead.
         *
         * See Telegram's [InlineQueryResultAudio](https://core.telegram.org/bots/api#inlinequeryresultaudio)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Audio internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Valid URL for the audio file. */
            val audioUrl: String,
            /** Title. */
            val title: String,
            /** Caption, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Performer. */
            val performer: String? = null,
            /** Audio duration. */
            val audioDuration: Duration? = null,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the audio. */
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalFormattedCaption {
            override val type: Type = Type.AUDIO

            /**
             * Case when the result represents an MP3 audio file stored on Telegram servers.
             *
             * By default, the audio file is sent by the user. [inputMessageContent] can be used to send the specified
             * content instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedAudio](https://core.telegram.org/bots/api#inlinequeryresultcachedaudio)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Valid file identifier for the audio file. */
                val audioFileId: String,
                /** Caption, 0-1024 characters after entities parsing. */
                override val caption: String? = null,
                /** Mode for parsing entities in [caption]. */
                override val parseMode: FormattedText.ParseMode? = null,
                /** Special entities that appear in [caption], specified instead of [parseMode]. */
                override val captionEntities: List<MessageEntity>? = null,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the audio. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalFormattedCaption {
                override val type: Type = Type.AUDIO

                companion object
            }

            companion object
        }


        /**
         * Case when the result represents a contact with a phone number.
         *
         * By default, the contact is sent by the user. [inputMessageContent] can be used to send the specified content
         * instead.
         *
         * See Telegram's [InlineQueryResultContact](https://core.telegram.org/bots/api#inlinequeryresultcontact)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Contact internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Contact's phone number. */
            val phoneNumber: String,
            /** Contact's first name. */
            val firstName: String,
            /** Contact's last name. */
            val lastName: String? = null,
            /** Additional data about the contact in the form of a vCard, 0-2048 bytes. */
            val vcard: String? = null,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the contact. */
            val inputMessageContent: InputMessageContent? = null,
            /** URL of the thumbnail for the result. */
            val thumbnailUrl: String? = null,
            /** Thumbnail width. */
            val thumbnailWidth: Int? = null,
            /** Thumbnail height. */
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type: Type = Type.CONTACT
            companion object
        }


        /**
         * Case when the result represents a link to a file.
         *
         * By default, the file is sent by the user. [inputMessageContent] can be used to send the specified content
         * instead. Only PDF and ZIP files can be sent.
         *
         * See Telegram's [InlineQueryResultDocument](https://core.telegram.org/bots/api#inlinequeryresultdocument)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Document internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Title of the result. */
            val title: String,
            /** Caption of the document, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Valid URL for the file. */
            val documentUrl: String,
            /** MIME type of the file, either `application/pdf` or `application/zip`. */
            val mimeType: String,
            /** Short description of the result. */
            val description: String? = null,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the file. */
            val inputMessageContent: InputMessageContent? = null,
            /** URL of the JPEG thumbnail for the file. */
            val thumbnailUrl: String? = null,
            /** Thumbnail width. */
            val thumbnailWidth: Int? = null,
            /** Thumbnail height. */
            val thumbnailHeight: Int? = null
        ) : Result, HasOptionalFormattedCaption {
            override val type: Type = Type.DOCUMENT

            /**
             * Case when the result represents a file stored on Telegram servers.
             *
             * By default, the file is sent by the user. [inputMessageContent] can be used to send the specified content
             * instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedDocument](https://core.telegram.org/bots/api#inlinequeryresultcacheddocument)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Title of the result. */
                val title: String,
                /** Valid file identifier for the file. */
                val documentFileId: String,
                /** Short description of the result. */
                val description: String? = null,
                /** Caption of the document, 0-1024 characters after entities parsing. */
                override val caption: String? = null,
                /** Mode for parsing entities in [caption]. */
                override val parseMode: FormattedText.ParseMode? = null,
                /** Special entities that appear in [caption], specified instead of [parseMode]. */
                override val captionEntities: List<MessageEntity>? = null,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the file. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalFormattedCaption {
                override val type: Type = Type.DOCUMENT

                companion object
            }

            companion object
        }

        /**
         * Case when the result represents a game.
         *
         * See Telegram's [InlineQueryResultGame](https://core.telegram.org/bots/api#inlinequeryresultgame)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Game internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Short name of the game. */
            val gameShortName: String,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null
        ) : Result {
            override val type: Type = Type.GAME

            companion object
        }

        /**
         * Case when the result represents a link to an animated GIF file.
         *
         * By default, the animation is sent by the user. [inputMessageContent] can be used to send the specified
         * content instead.
         *
         * See Telegram's [InlineQueryResultGif](https://core.telegram.org/bots/api#inlinequeryresultgif) documentation.
         */
        @TelegramCodegen.Type
        data class Gif internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Valid URL for the GIF file. */
            val gifUrl: String,
            /** Width of the GIF. */
            val gifWidth: Int? = null,
            /** Height of the GIF. */
            val gifHeight: Int? = null,
            /** Duration of the GIF. */
            val gifDuration: Duration? = null,
            /** URL of the static JPEG or GIF, or animated MPEG-4 thumbnail for the result. */
            val thumbnailUrl: String,
            /** MIME type of the thumbnail: `image/jpeg`, `image/gif`, or `video/mp4`. */
            val thumbnailMimeType: String? = null,
            /** Title of the result. */
            val title: String? = null,
            /** Caption of the GIF, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Whether the caption must be shown above the message media. */
            val showCaptionAboveMedia: Boolean = false,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the GIF animation. */
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalFormattedCaption {
            override val type: Type = Type.GIF

            /**
             * Case when the result represents an animated GIF file stored on Telegram servers.
             *
             * By default, the animation is sent by the user. [inputMessageContent] can be used to send the specified
             * content instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedGif](https://core.telegram.org/bots/api#inlinequeryresultcachedgif) documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Valid file identifier for the GIF file. */
                val gifFileId: String,
                /** Title of the result. */
                val title: String? = null,
                /** Caption of the GIF, 0-1024 characters after entities parsing. */
                override val caption: String? = null,
                /** Mode for parsing entities in [caption]. */
                override val parseMode: FormattedText.ParseMode? = null,
                /** Special entities that appear in [caption], specified instead of [parseMode]. */
                override val captionEntities: List<MessageEntity>? = null,
                /** Whether the caption must be shown above the message media. */
                val showCaptionAboveMedia: Boolean = false,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the GIF animation. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalFormattedCaption {
                override val type: Type = Type.GIF

                companion object
            }

            companion object
        }

        /**
         * Case when the result represents a location on a map.
         *
         * By default, the location is sent by the user. [inputMessageContent] can be used to send the specified content
         * instead.
         *
         * See Telegram's [InlineQueryResultLocation](https://core.telegram.org/bots/api#inlinequeryresultlocation)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Location internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Location latitude in degrees. */
            val latitude: Double,
            /** Location longitude in degrees. */
            val longitude: Double,
            /** Location title. */
            val title: String,
            /** Radius of uncertainty for the location in meters; 0-1500. */
            val horizontalAccuracy: Double? = null,
            /**
             * Period during which the location can be updated; 60-86400 seconds, or `0x7FFFFFFF` for a live location
             * that can be edited indefinitely.
             */
            val livePeriod: Duration? = null,
            /** For live locations, direction in which the user is moving, in degrees; 1-360. */
            val heading: Int? = null,
            /** For live locations, maximum distance for proximity alerts, in meters; 1-100000. */
            val proximityAlertRadius: Int? = null,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the location. */
            val inputMessageContent: InputMessageContent? = null,
            /** URL of the thumbnail for the result. */
            val thumbnailUrl: String? = null,
            /** Thumbnail width. */
            val thumbnailWidth: Int? = null,
            /** Thumbnail height. */
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type: Type = Type.LOCATION

            companion object
        }


        /**
         * Case when the result represents a link to an H.264/MPEG-4 AVC video animation without sound.
         *
         * By default, the animation is sent by the user. [inputMessageContent] can be used to send the specified
         * content instead.
         *
         * See Telegram's [InlineQueryResultMpeg4Gif](https://core.telegram.org/bots/api#inlinequeryresultmpeg4gif)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Mpeg4Gif internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Valid URL for the MPEG-4 file. */
            val mpeg4Url: String,
            /** Video width. */
            val mpeg4Width: Int? = null,
            /** Video height. */
            val mpeg4Height: Int? = null,
            /** Video duration. */
            val mpeg4Duration: Duration? = null,
            /** URL of the static JPEG or GIF, or animated MPEG-4 thumbnail for the result. */
            val thumbnailUrl: String,
            /** MIME type of the thumbnail: `image/jpeg`, `image/gif`, or `video/mp4`. */
            val thumbnailMimeType: String? = null,
            /** Title of the result. */
            val title: String? = null,
            /** Caption of the MPEG-4 file, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Whether the caption must be shown above the message media. */
            val showCaptionAboveMedia: Boolean = false,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the video animation. */
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalFormattedCaption {
            override val type: Type = Type.MPEG4_GIF

            /**
             * Case when the result represents an MPEG-4 animation stored on Telegram servers.
             *
             * By default, the animation is sent by the user. [inputMessageContent] can be used to send the specified
             * content instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedMpeg4Gif](https://core.telegram.org/bots/api#inlinequeryresultcachedmpeg4gif)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Valid file identifier for the MPEG-4 file. */
                val mpeg4FileId: String,
                /** Title of the result. */
                val title: String? = null,
                /** Caption of the MPEG-4 file, 0-1024 characters after entities parsing. */
                override val caption: String? = null,
                /** Mode for parsing entities in [caption]. */
                override val parseMode: FormattedText.ParseMode? = null,
                /** Special entities that appear in [caption], specified instead of [parseMode]. */
                override val captionEntities: List<MessageEntity>? = null,
                /** Whether the caption must be shown above the message media. */
                val showCaptionAboveMedia: Boolean = false,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the video animation. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalFormattedCaption {
                override val type: Type = Type.MPEG4_GIF

                companion object
            }

            companion object
        }


        /**
         * Case when the result represents a link to a photo.
         *
         * By default, the photo is sent by the user. [inputMessageContent] can be used to send the specified content
         * instead.
         *
         * See Telegram's [InlineQueryResultPhoto](https://core.telegram.org/bots/api#inlinequeryresultphoto)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Photo internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Valid URL of a JPEG photo no larger than 5 MB. */
            val photoUrl: String,
            /** URL of the thumbnail for the photo. */
            val thumbnailUrl: String,
            /** Width of the photo. */
            val photoWidth: Int? = null,
            /** Height of the photo. */
            val photoHeight: Int? = null,
            /** Title of the result. */
            val title: String? = null,
            /** Short description of the result. */
            val description: String? = null,
            /** Caption of the photo, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Whether the caption must be shown above the message media. */
            val showCaptionAboveMedia: Boolean = false,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the photo. */
            val inputMessageContent: InputMessageContent? = null

        ) : Result, HasOptionalFormattedCaption {
            override val type: Type = Type.PHOTO

            /**
             * Case when the result represents a photo stored on Telegram servers.
             *
             * By default, the photo is sent by the user. [inputMessageContent] can be used to send the specified
             * content instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedPhoto](https://core.telegram.org/bots/api#inlinequeryresultcachedphoto)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Valid file identifier for the photo. */
                val photoFileId: String,
                /** Title of the result. */
                val title: String? = null,
                /** Short description of the result. */
                val description: String? = null,
                /** Caption of the photo, 0-1024 characters after entities parsing. */
                override val caption: String? = null,
                /** Mode for parsing entities in [caption]. */
                override val parseMode: FormattedText.ParseMode? = null,
                /** Special entities that appear in [caption], specified instead of [parseMode]. */
                override val captionEntities: List<MessageEntity>? = null,
                /** Whether the caption must be shown above the message media. */
                val showCaptionAboveMedia: Boolean = false,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the photo. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalFormattedCaption {
                override val type: Type = Type.PHOTO

                companion object
            }

            companion object
        }

        object Sticker {
            /**
             * Case when the result represents a sticker stored on Telegram servers.
             *
             * By default, the sticker is sent by the user. [inputMessageContent] can be used to send the specified
             * content instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedSticker](https://core.telegram.org/bots/api#inlinequeryresultcachedsticker)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Valid file identifier for the sticker. */
                val stickerFileId: String,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the sticker. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result {
                override val type = Type.STICKER

                companion object
            }
        }

        /**
         * Case when the result represents a venue.
         *
         * By default, the venue is sent by the user. [inputMessageContent] can be used to send the specified content
         * instead.
         *
         * See Telegram's [InlineQueryResultVenue](https://core.telegram.org/bots/api#inlinequeryresultvenue)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Venue internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Latitude of the venue location in degrees. */
            val latitude: Double,
            /** Longitude of the venue location in degrees. */
            val longitude: Double,
            /** Title of the venue. */
            val title: String,
            /** Address of the venue. */
            val address: String,
            /** Foursquare identifier of the venue, if known. */
            val foursquareId: String? = null,
            /** Foursquare type of the venue, if known. */
            val foursquareType: String? = null,
            /** Google Places identifier of the venue. */
            val googlePlaceId: String? = null,
            /** Google Places type of the venue. */
            val googlePlaceType: String? = null,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the venue. */
            val inputMessageContent: InputMessageContent? = null,
            /** URL of the thumbnail for the result. */
            val thumbnailUrl: String? = null,
            /** Thumbnail width. */
            val thumbnailWidth: Int? = null,
            /** Thumbnail height. */
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type: Type = Type.VENUE

            companion object
        }

        /**
         * Case when the result represents a link to a page containing an embedded video player or a video file.
         *
         * By default, the video is sent by the user. [inputMessageContent] must be used for an embedded video and can
         * be used to send specified content instead of a video file.
         *
         * See Telegram's [InlineQueryResultVideo](https://core.telegram.org/bots/api#inlinequeryresultvideo)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Video internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Valid URL for the embedded video player or video file. */
            val videoUrl: String,
            /** MIME type of [videoUrl], either `text/html` or `video/mp4`. */
            val mimeType: String,
            /** URL of the JPEG thumbnail for the video. */
            val thumbnailUrl: String,
            /** Title of the result. */
            val title: String,
            /** Caption of the video, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Whether the caption must be shown above the message media. */
            val showCaptionAboveMedia: Boolean = false,
            /** Video width. */
            val videoWidth: Int? = null,
            /** Video height. */
            val videoHeight: Int? = null,
            /** Video duration. */
            val videoDuration: Duration? = null,
            /** Short description of the result. */
            val description: String? = null,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content to send instead of the video; required when [videoUrl] points to an HTML page. */
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalFormattedCaption {
            override val type: Type = Type.VIDEO

            /**
             * Case when the result represents a video file stored on Telegram servers.
             *
             * By default, the video is sent by the user. [inputMessageContent] can be used to send the specified
             * content instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedVideo](https://core.telegram.org/bots/api#inlinequeryresultcachedvideo)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Valid file identifier for the video file. */
                val videoFileId: String,
                /** Title of the result. */
                val title: String,
                /** Short description of the result. */
                val description: String? = null,
                /** Caption of the video, 0-1024 characters after entities parsing. */
                override val caption: String? = null,
                /** Mode for parsing entities in [caption]. */
                override val parseMode: FormattedText.ParseMode? = null,
                /** Special entities that appear in [caption], specified instead of [parseMode]. */
                override val captionEntities: List<MessageEntity>? = null,
                /** Whether the caption must be shown above the message media. */
                val showCaptionAboveMedia: Boolean = false,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the video. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalFormattedCaption {
                override val type: Type = Type.VIDEO

                companion object
            }

            companion object
        }

        /**
         * Case when the result represents a link to an OGG voice recording encoded with OPUS.
         *
         * By default, the voice recording is sent by the user. [inputMessageContent] can be used to send the specified
         * content instead.
         *
         * See Telegram's [InlineQueryResultVoice](https://core.telegram.org/bots/api#inlinequeryresultvoice)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Voice internal constructor(
            /** Unique identifier for this result, 1-64 bytes. */
            override val id: String,
            /** Valid URL for the voice recording. */
            val voiceUrl: String,
            /** Recording title. */
            val title: String,
            /** Caption, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Recording duration. */
            val voiceDuration: Duration? = null,
            /** Inline keyboard attached to the message. */
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            /** Content of the message to be sent instead of the voice recording. */
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalFormattedCaption {
            override val type: Type = Type.VOICE

            /**
             * Case when the result represents a voice recording stored on Telegram servers.
             *
             * By default, the voice recording is sent by the user. [inputMessageContent] can be used to send the
             * specified content instead.
             *
             * See Telegram's
             * [InlineQueryResultCachedVoice](https://core.telegram.org/bots/api#inlinequeryresultcachedvoice)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Cached internal constructor(
                /** Unique identifier for this result, 1-64 bytes. */
                override val id: String,
                /** Valid file identifier for the voice recording. */
                val voiceFileId: String,
                /** Voice message title. */
                val title: String,
                /** Caption, 0-1024 characters after entities parsing. */
                override val caption: String? = null,
                /** Mode for parsing entities in [caption]. */
                override val parseMode: FormattedText.ParseMode? = null,
                /** Special entities that appear in [caption], specified instead of [parseMode]. */
                override val captionEntities: List<MessageEntity>? = null,
                /** Inline keyboard attached to the message. */
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                /** Content of the message to be sent instead of the voice recording. */
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalFormattedCaption {
                override val type: Type = Type.VOICE

                companion object
            }


            companion object
        }


        enum class Type {
            @JsonProperty(ARTICLE_STR)
            ARTICLE,
            @JsonProperty(PHOTO_STR)
            PHOTO,
            @JsonProperty(GIF_STR)
            GIF,
            @JsonProperty(MPEG4_GIF_STR)
            MPEG4_GIF,
            @JsonProperty(VIDEO_STR)
            VIDEO,
            @JsonProperty(AUDIO_STR)
            AUDIO,
            @JsonProperty(VOICE_STR)
            VOICE,
            @JsonProperty(DOCUMENT_STR)
            DOCUMENT,
            @JsonProperty(LOCATION_STR)
            LOCATION,
            @JsonProperty(VENUE_STR)
            VENUE,
            @JsonProperty(CONTACT_STR)
            CONTACT,
            @JsonProperty(GAME_STR)
            GAME,
            @JsonProperty(STICKER_STR)
            STICKER;

            companion object {
                const val ARTICLE_STR = "article"
                const val PHOTO_STR = "photo"
                const val GIF_STR = "gif"
                const val MPEG4_GIF_STR = "mpeg4_gif"
                const val VIDEO_STR = "video"
                const val AUDIO_STR = "audio"
                const val VOICE_STR = "voice"
                const val DOCUMENT_STR = "document"
                const val LOCATION_STR = "location"
                const val VENUE_STR = "venue"
                const val CONTACT_STR = "contact"
                const val GAME_STR = "game"
                const val STICKER_STR = "sticker"
            }
        }

    }
    companion object
}
