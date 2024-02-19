package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.jackson.typing.TypeResolverWithDeductionBuilder
import ski.gagar.vertigram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.types.richtext.HasRichText
import ski.gagar.vertigram.types.richtext.RichText
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [InlineQuery](https://core.telegram.org/bots/api#inlinequery) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class InlineQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    val chatType: Chat.Type? = null,
    val location: Location? = null
) {

    /**
     * Telegram [InputMessageContent](https://core.telegram.org/bots/api#inputtextmessagecontent) type.
     * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
     * names given they are nested into [InlineQuery.InputMessageContent] class. The rule here is the following:
     * `InputXxxMessageContent` Telegram type becomes `InlineQuery.InputMessageContent.Xxx`.
     * For up-to-date documentation please consult the official Telegram docs.
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
    )
    sealed interface InputMessageContent {
        /**
         * Telegram [InputTextMessageContent](https://core.telegram.org/bots/api#inputtextmessagecontent) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Text internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val messageText: String,
            override val parseMode: RichText.ParseMode? = null,
            override val entities: List<MessageEntity>? = null,
            val linkPreviewOptions: Message.LinkPreviewOptions? = null
        ) : InputMessageContent, HasRichText {
            @JsonIgnore
            override val text = messageText

            companion object {
                /**
                 * Manually generated pseudo-constructor.
                 *
                 * This is the only case when text parameter is named [messageText] instead of `text`,
                 * therefore code generation is not working yet
                 */
                operator fun invoke(
                    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                    richText: RichText,
                    linkPreviewOptions: Message.LinkPreviewOptions? = null
                ) = Text(
                    messageText = richText.text,
                    parseMode = richText.parseMode,
                    entities = richText.entities,
                    linkPreviewOptions = linkPreviewOptions
                )
            }
        }

        /**
         * Telegram [InputLocationMessageContent](https://core.telegram.org/bots/api#inputlocationmessagecontent) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Location(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val latitude: Double,
            val longitude: Double,
            val horizontalAccuracy: Double? = null,
            val livePeriod: Duration? = null,
            val heading: Int? = null,
            val proximityAlertRadius: Int? = null,
        ) : InputMessageContent

        /**
         * Telegram [InputVenueMessageContent](https://core.telegram.org/bots/api#inputvenuemessagecontent) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Venue(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val latitude: Double,
            val longitude: Double,
            val title: String,
            val address: String,
            val foursquareId: String? = null,
            val foursquareType: String? = null,
            val googlePlaceId: String? = null,
            val googlePlaceType: String? = null
        ) : InputMessageContent

        /**
         * Telegram [InputContactMessageContent](https://core.telegram.org/bots/api#inputcontactmessagecontent) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Contact(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val phoneNumber: String,
            val firstName: String,
            val lastName: String? = null,
            val vcard: String? = null
        ) : InputMessageContent

        /**
         * Telegram [InputInvoiceMessageContent](https://core.telegram.org/bots/api#inputinvoicemessagecontent) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Invoice(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val title: String,
            val description: String,
            val payload: String,
            val providerToken: String,
            val currency: String,
            val prices: List<LabeledPrice>,
            val maxTipAmount: Int? = null,
            val suggestedTipAmounts: List<Int>? = null,
            val providerData: String? = null,
            val photoUrl: String? = null,
            val photoSize: Long? = null,
            val photoWidth: Int? = null,
            val photoHeight: Int? = null,
            val needName: Boolean = false,
            val needPhoneNumber: Boolean = false,
            val needEmail: Boolean = false,
            val needShippingAddress: Boolean = false,
            val sendPhoneNumberToProvider: Boolean = false,
            val sendEmailToProvider: Boolean = false,
            @get:JvmName("getIsFlexible")
            val isFlexible: Boolean? = false
        ) : InputMessageContent
    }


    /**
     * Telegram [InlineQueryResult](https://core.telegram.org/bots/api#inlinequeryresult) type.
     *
     * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
     * names given they are nested into [InlineQuery.Result] class. The rule here is the following:
     * `InlineQueryResultXxx` Telegram type becomes `InlineQuery.Result.Xxx` and
     * `InlineQueryResultCachedXxx` Telegram type becomes `InlineQuery.Result.Xxx.Cached`.
     *
     * For up-to-date documentation please consult the official Telegram docs.
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
        // Omitted intentionaly there is no such class on telegram
        // JsonSubTypes.Type(value = Result.Sticker::class, name = Result.Type.STICKER_STR),
        JsonSubTypes.Type(value = Result.Sticker.Cached::class, name = Result.Type.STICKER_STR),
        JsonSubTypes.Type(value = Result.Venue::class, name = Result.Type.VENUE_STR),
        JsonSubTypes.Type(value = Result.Video::class, name = Result.Type.VIDEO_STR),
        JsonSubTypes.Type(value = Result.Video.Cached::class, name = Result.Type.VIDEO_STR),
        JsonSubTypes.Type(value = Result.Voice::class, name = Result.Type.VOICE_STR),
        JsonSubTypes.Type(value = Result.Voice.Cached::class, name = Result.Type.VOICE_STR),

        )
    @JsonTypeResolver(TypeResolverWithDeductionBuilder::class)
    sealed interface Result {
        val type: Type
        val id: String

        /**
         * Telegram [InlineQueryResultsButton](https://core.telegram.org/bots/api#inlinequeryresultsbutton) type.
         *
         * This class is NOT a subclass of [Result]
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Button(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val text: String,
            val webApp: WebAppInfo? = null,
            val startParameter: String? = null
        )

        /**
         * Telegram [InlineQueryResultArticle](https://core.telegram.org/bots/api#inlinequeryresultarticle) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Article internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val title: String,
            val inputMessageContent: InputMessageContent,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val url: String? = null,
            val hideUrl: Boolean = false,
            val description: String? = null,
            val thumbnailUrl: String? = null,
            val thumbnailWidth: Int? = null,
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type = Type.ARTICLE

            companion object;
        }

        /**
         * Telegram [InlineQueryResultAudio](https://core.telegram.org/bots/api#inlinequeryresultaudio) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Audio internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val audioUrl: String,
            val title: String,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val performer: String? = null,
            val audioDuration: Duration? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalRichCaption {
            override val type: Type = Type.AUDIO

            /**
             * Telegram [InlineQueryResultCachedAudio](https://core.telegram.org/bots/api#inlinequeryresultcachedaudio) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val audioFileId: String,
                override val caption: String? = null,
                override val parseMode: RichText.ParseMode? = null,
                override val captionEntities: List<MessageEntity>? = null,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalRichCaption {
                override val type: Type = Type.AUDIO

                companion object
            }

            companion object
        }


        /**
         * Telegram [InlineQueryResultContact](https://core.telegram.org/bots/api#inlinequeryresultcontact) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Contact(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val phoneNumber: String,
            val firstName: String,
            val lastName: String? = null,
            val vcard: String? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null,
            val thumbnailUrl: String? = null,
            val thumbnailWidth: Int? = null,
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type: Type = Type.CONTACT
        }


        /**
         * Telegram [InlineQueryResultDocument](https://core.telegram.org/bots/api#inlinequeryresultdocument) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Document internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val title: String,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val documentUrl: String,
            val mimeType: String,
            val description: String? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null,
            val thumbnailUrl: String? = null,
            val thumbnailWidth: Int? = null,
            val thumbnailHeight: Int? = null
        ) : Result, HasOptionalRichCaption {
            override val type: Type = Type.DOCUMENT

            /**
             * Telegram [InlineQueryResultCachedDocument](https://core.telegram.org/bots/api#inlinequeryresultcacheddocument) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val title: String,
                val documentFileId: String,
                val description: String? = null,
                override val caption: String? = null,
                override val parseMode: RichText.ParseMode? = null,
                override val captionEntities: List<MessageEntity>? = null,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalRichCaption {
                override val type: Type = Type.DOCUMENT

                companion object
            }

            companion object
        }

        /**
         * Telegram [InlineQueryResultGame](https://core.telegram.org/bots/api#inlinequeryresultgame) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Game(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val gameShortName: String,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null
        ) : Result {
            override val type: Type = Type.GAME
        }

        /**
         * Telegram [InlineQueryResultGif](https://core.telegram.org/bots/api#inlinequeryresultgif) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Gif internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val gifUrl: String,
            val gifWidth: Int? = null,
            val gifHeight: Int? = null,
            val gifDuration: Duration? = null,
            val thumbnailUrl: String,
            val thumbnailMimeType: String? = null,
            val title: String? = null,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalRichCaption {
            override val type: Type = Type.GIF

            /**
             * Telegram [InlineQueryResultCachedGif](https://core.telegram.org/bots/api#inlinequeryresultcachedgif) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val gifFileId: String,
                val title: String? = null,
                override val caption: String? = null,
                override val parseMode: RichText.ParseMode? = null,
                override val captionEntities: List<MessageEntity>? = null,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalRichCaption {
                override val type: Type = Type.GIF

                companion object
            }

            companion object
        }

        /**
         * Telegram [InlineQueryResultLocation](https://core.telegram.org/bots/api#inlinequeryresultlocation) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Location(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val latitude: Double,
            val longitude: Double,
            val title: String,
            val horizontalAccuracy: Double? = null,
            val livePeriod: Duration? = null,
            val heading: Int? = null,
            val proximityAlertRadius: Int? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null,
            val thumbnailUrl: String? = null,
            val thumbnailWidth: Int? = null,
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type: Type = Type.LOCATION
        }


        /**
         * Telegram [InlineQueryResultMpeg4Gif](https://core.telegram.org/bots/api#inlinequeryresultmpeg4gif) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Mpeg4Gif internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val mpeg4Url: String,
            val mpeg4Width: Int? = null,
            val mpeg4Height: Int? = null,
            val mpeg4Duration: Duration? = null,
            val thumbnailUrl: String,
            val thumbnailMimeType: String? = null,
            val title: String? = null,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalRichCaption {
            override val type: Type = Type.MPEG4_GIF

            /**
             * Telegram [InlineQueryResultCachedMpeg4Gif](https://core.telegram.org/bots/api#inlinequeryresultcachedmpeg4gif) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val mpeg4FileId: String,
                val title: String? = null,
                override val caption: String? = null,
                override val parseMode: RichText.ParseMode? = null,
                override val captionEntities: List<MessageEntity>? = null,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalRichCaption {
                override val type: Type = Type.MPEG4_GIF

                companion object
            }

            companion object
        }


        /**
         * Telegram [InlineQueryResultPhoto](https://core.telegram.org/bots/api#inlinequeryresultphoto) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Photo internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val photoUrl: String,
            val thumbnailUrl: String,
            val photoWidth: Int? = null,
            val photoHeight: Int? = null,
            val title: String? = null,
            val description: String? = null,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null

        ) : Result, HasOptionalRichCaption {
            override val type: Type = Type.PHOTO

            /**
             * Telegram [InlineQueryResultCachedPhoto](https://core.telegram.org/bots/api#inlinequeryresultcachedphoto) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val photoFileId: String,
                val title: String? = null,
                val description: String? = null,
                override val caption: String? = null,
                override val parseMode: RichText.ParseMode? = null,
                override val captionEntities: List<MessageEntity>? = null,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalRichCaption {
                override val type: Type = Type.PHOTO

                companion object
            }

            companion object
        }

        object Sticker {
            /**
             * Telegram [InlineQueryResultCachedSticker](https://core.telegram.org/bots/api#inlinequeryresultcachedsticker) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val stickerFileId: String,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result {
                override val type = Type.STICKER

                companion object
            }
        }

        /**
         * Telegram [InlineQueryResultVenue](https://core.telegram.org/bots/api#inlinequeryresultvenue) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Venue internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val latitude: Double,
            val longitude: Double,
            val title: String,
            val address: String,
            val foursquareId: String? = null,
            val foursquareType: String? = null,
            val googlePlaceId: String? = null,
            val googlePlaceType: String? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null,
            val thumbnailUrl: String? = null,
            val thumbnailWidth: Int? = null,
            val thumbnailHeight: Int? = null
        ) : Result {
            override val type: Type = Type.VENUE
        }

        /**
         * Telegram [InlineQueryResultVideo](https://core.telegram.org/bots/api#inlinequeryresultvideo) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Video internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val videoUrl: String,
            val mimeType: String,
            val thumbnailUrl: String,
            val title: String,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val videoWidth: Int? = null,
            val videoHeight: Int? = null,
            val videoDuration: Duration? = null,
            val description: String? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalRichCaption {
            override val type: Type = Type.VIDEO

            /**
             * Telegram [InlineQueryResultCachedVideo](https://core.telegram.org/bots/api#inlinequeryresultcachedvideo) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val videoFileId: String,
                val title: String,
                val description: String? = null,
                override val caption: String? = null,
                override val parseMode: RichText.ParseMode? = null,
                override val captionEntities: List<MessageEntity>? = null,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalRichCaption {
                override val type: Type = Type.VIDEO

                companion object
            }

            companion object
        }

        /**
         * Telegram [InlineQueryResultVoice](https://core.telegram.org/bots/api#inlinequeryresultvoice) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen(
            generateMethod = false,
            generatePseudoConstructor = true
        )
        data class Voice internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val id: String,
            val voiceUrl: String,
            val title: String,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val voiceDuration: Duration? = null,
            val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
            val inputMessageContent: InputMessageContent? = null
        ) : Result, HasOptionalRichCaption {
            override val type: Type = Type.VOICE

            /**
             * Telegram [InlineQueryResultCachedVoice](https://core.telegram.org/bots/api#inlinequeryresultcachedvoice) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen(
                generateMethod = false,
                generatePseudoConstructor = true
            )
            data class Cached internal constructor(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                override val id: String,
                val voiceFileId: String,
                val title: String,
                override val caption: String? = null,
                override val parseMode: RichText.ParseMode? = null,
                override val captionEntities: List<MessageEntity>? = null,
                val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
                val inputMessageContent: InputMessageContent? = null
            ) : Result, HasOptionalRichCaption {
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
}
