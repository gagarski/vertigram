package ski.gagar.vertigram.telegram.methods

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.InputRichBlock
import ski.gagar.vertigram.telegram.types.InputRichMessage
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.attachments.fileId
import ski.gagar.vertigram.telegram.types.methods.*
import ski.gagar.vertigram.telegram.types.richmessage.RichTextValue
import ski.gagar.vertigram.telegram.types.util.toChatId
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

object MethodsTest : BaseSerializationTest() {
    @Test
    fun `answerPreCheckoutQuery should survive serialization`() {
        assertSerializable<AnswerPreCheckoutQuery>(
            AnswerPreCheckoutQuery.Ok(
                preCheckoutQueryId = "1"
            )
        )
        assertSerializable<AnswerPreCheckoutQuery>(
            AnswerPreCheckoutQuery.Error(
                preCheckoutQueryId = "1",
                errorMessage = "oops"
            )
        )
    }

    @Test
    fun `answerShippingQuery should survive serialization`() {
        assertSerializable<AnswerShippingQuery>(
            AnswerShippingQuery.Ok(
                shippingQueryId = "1",
                shippingOptions = listOf()
            )
        )
        assertSerializable<AnswerShippingQuery>(
            AnswerShippingQuery.Error(
                shippingQueryId = "1",
                errorMessage = "oops"
            )
        )
    }

    @Test
    fun `createChatInviteLink should survive serialization`() {
        assertSerializable<CreateChatInviteLink>(
            CreateChatInviteLink.WithJoinRequest(
                chatId = 1.toChatId(),
                createsJoinRequest = true
            )
        )
        assertSerializable<CreateChatInviteLink>(
            CreateChatInviteLink.WithMemberLimit(
                chatId = 1.toChatId(),
                memberLimit = 1
            )
        )
    }

    @Test
    fun `editChatInviteLink should survive serialization`() {
        assertSerializable<EditChatInviteLink>(
            EditChatInviteLink.WithJoinRequest(
                chatId = 1.toChatId(),
                createsJoinRequest = true,
                inviteLink = "xxx"
            )
        )
        assertSerializable<EditChatInviteLink>(
            EditChatInviteLink.WithMemberLimit(
                chatId = 1.toChatId(),
                memberLimit = 1,
                inviteLink = "xxx"
            )
        )
    }

    @Test
    fun `editMessageCaption should survive serialization`() {
        assertSerializable<EditMessageCaption>(
            EditMessageCaption.InlineMessage(
                inlineMessageId = "1"
            )
        )
        assertSerializable<EditMessageCaption>(
            EditMessageCaption.ChatMessage(
                chatId = 1.toChatId(),
                messageId = 1
            )
        )
    }

    @Test
    fun `editMessageLiveLocation should survive serialization`() {
        assertSerializable<EditMessageLiveLocation>(
            EditMessageLiveLocation.InlineMessage(
                inlineMessageId = "1",
                latitude = 0.0,
                longitude = 0.0
            )
        )
        assertSerializable<EditMessageLiveLocation>(
            EditMessageLiveLocation.ChatMessage(
                chatId = 1.toChatId(),
                messageId = 1,
                latitude = 0.0,
                longitude = 0.0
            )
        )
    }

    @Test
    fun `editMessageMedia should survive serialization`() {
        assertSerializable<EditMessageMedia>(
            EditMessageMedia.InlineMessage(
                inlineMessageId = "1",
                media = InputMedia.Audio(
                    media = Attachment.fileId("1")
                )
            ),
            skip = setOf(Companion.Mappers.TELEGRAM) // deserialization of attachment is not supported here
        )
        assertSerializable<EditMessageMedia>(
            EditMessageMedia.ChatMessage(
                chatId = 1.toChatId(),
                messageId = 1,
                media = InputMedia.Audio(
                    media = Attachment.fileId("1")
                )
            ),
            skip = setOf(Companion.Mappers.TELEGRAM) // deserialization of attachment is not supported here
        )
    }

    @Test
    fun `editReplyMessageMarkup should survive serialization`() {
        assertSerializable<EditMessageReplyMarkup>(
            EditMessageReplyMarkup.InlineMessage(
                inlineMessageId = "1"
            )
        )
        assertSerializable<EditMessageReplyMarkup>(
            EditMessageReplyMarkup.ChatMessage(
                chatId = 1.toChatId(),
                messageId = 1
            )
        )
    }

    @Test
    fun `editMessageText should survive serialization`() {
        assertSerializable<EditMessageText>(
            EditMessageText.InlineMessage(
                inlineMessageId = "1",
                text = "aaa",
                richMessage = InputRichMessage.Html(html = "<p>aaa</p>")
            )
        )
        assertSerializable<EditMessageText>(
            EditMessageText.ChatMessage(
                chatId = 1.toChatId(),
                messageId = 1,
                text = "aaa",
                richMessage = InputRichMessage.Html(html = "<p>aaa</p>")
            )
        )
    }

    @Test
    fun `rich message methods should survive serialization`() {
        val richMessages = listOf(
            InputRichMessage.Html(html = "<p>aaa</p>"),
            InputRichMessage.Markdown(markdown = "aaa"),
            InputRichMessage.Blocks(
                blocks = listOf(
                    InputRichBlock.Paragraph(
                        text = RichTextValue.plain("aaa")
                    )
                )
            )
        )

        for (richMessage in richMessages) {
            assertSerializable<SendRichMessage>(
                SendRichMessage(
                    chatId = 1.toChatId(),
                    richMessage = richMessage
                )
            )
            assertSerializable<SendRichMessageDraft>(
                SendRichMessageDraft(
                    chatId = 1,
                    draftId = 2,
                    richMessage = richMessage
                )
            )
        }
    }

    @Test
    fun `ephemeral message methods should survive serialization`() {
        assertSerializable<EditEphemeralMessageText>(
            EditEphemeralMessageText(
                chatId = 1.toChatId(),
                receiverUserId = 2,
                ephemeralMessageId = 3,
                text = "edited"
            )
        )
        assertSerializable<EditEphemeralMessageMedia>(
            EditEphemeralMessageMedia(
                chatId = 1.toChatId(),
                receiverUserId = 2,
                ephemeralMessageId = 3,
                media = InputMedia.Photo(
                    media = Attachment.fileId("photo")
                )
            ),
            skip = setOf(Companion.Mappers.TELEGRAM)
        )
        assertSerializable<EditEphemeralMessageCaption>(
            EditEphemeralMessageCaption(
                chatId = 1.toChatId(),
                receiverUserId = 2,
                ephemeralMessageId = 3,
                caption = "edited"
            )
        )
        assertSerializable<EditEphemeralMessageReplyMarkup>(
            EditEphemeralMessageReplyMarkup(
                chatId = 1.toChatId(),
                receiverUserId = 2,
                ephemeralMessageId = 3
            )
        )
        assertSerializable<DeleteEphemeralMessage>(
            DeleteEphemeralMessage(
                chatId = 1.toChatId(),
                receiverUserId = 2,
                ephemeralMessageId = 3
            )
        )
    }

    @Test
    fun `chat join request query methods should survive serialization`() {
        assertSerializable<AnswerChatJoinRequestQuery>(
            AnswerChatJoinRequestQuery(
                chatJoinRequestQueryId = "1",
                result = AnswerChatJoinRequestQuery.Result.APPROVE
            )
        )
        assertSerializable<SendChatJoinRequestWebApp>(
            SendChatJoinRequestWebApp(
                chatJoinRequestQueryId = "1",
                webAppUrl = "https://example.com"
            )
        )
    }

    @Test
    fun `business methods should survive serialization`() {
        assertSerializable<GetBusinessConnection>(
            GetBusinessConnection(
                businessConnectionId = "1"
            )
        )
        assertSerializable<DeleteBusinessMessages>(
            DeleteBusinessMessages(
                businessConnectionId = "1",
                messageIds = listOf(1)
            )
        )
    }

    @Test
    fun `premium gift method should survive serialization`() {
        assertSerializable<GiftPremiumSubscription>(
            GiftPremiumSubscription(
                userId = 1,
                monthCount = GiftPremiumSubscription.MonthCount.THREE,
                text = "gift"
            )
        )
        assertSerializable<UpgradeGift>(
            UpgradeGift(
                businessConnectionId = "1",
                ownedGiftId = "gift",
                keepOriginalDetails = true,
                starCount = 1
            )
        )
        assertSerializable<TransferGift>(
            TransferGift(
                businessConnectionId = "1",
                ownedGiftId = "gift",
                newOwnerChatId = 1,
                starCount = 1
            )
        )
    }

    @Test
    fun `getGameHighScores should survive serialization`() {
        assertSerializable<GetGameHighScores>(
            GetGameHighScores.InlineMessage(
                inlineMessageId = "1",
                userId = 1
            )
        )
        assertSerializable<GetGameHighScores>(
            GetGameHighScores.ChatMessage(
                chatId = 1,
                messageId = 1,
                userId = 1
            )
        )
    }

    @Test
    fun `send methods with message effect should survive serialization`() {
        assertSerializable<SendGame>(
            SendGame(
                chatId = 1.toChatId(),
                gameShortName = "game",
                messageEffectId = "effect"
            )
        )
        assertSerializable<SendInvoice>(
            SendInvoice(
                chatId = 1.toChatId(),
                title = "title",
                description = "description",
                payload = "payload",
                currency = "XTR",
                prices = listOf(),
                messageEffectId = "effect"
            )
        )
        assertSerializable<SendMediaGroup>(
            SendMediaGroup(
                chatId = 1.toChatId(),
                media = listOf(InputMedia.Photo(media = Attachment.fileId("1"))),
                messageEffectId = "effect"
            ),
            skip = setOf(Companion.Mappers.TELEGRAM) // deserialization of attachment is not supported here
        )
    }

    @Test
    fun `sendPoll and sendQuiz should survive serialization`() {
        assertSerializable<SendPoll>(
            SendPoll.Regular.OpenPeriod(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                openPeriod = Duration.ofHours(1)
            )
        )
        assertSerializable<SendPoll.Regular.OpenPeriod>(
            SendPoll.Regular.OpenPeriod(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                openPeriod = Duration.ofHours(1)
            )
        )
        assertSerializable<SendPoll>(
            SendPoll.Regular.CloseDate(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                closeDate = Instant.now().truncatedTo(ChronoUnit.SECONDS)
            )
        )
        assertSerializable<SendPoll.Regular.CloseDate>(
            SendPoll.Regular.CloseDate(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                closeDate = Instant.now().truncatedTo(ChronoUnit.SECONDS)
            )
        )
        assertSerializable<SendPoll>(
            SendPoll.Regular.Indefinite(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(
                    SendPoll.InputOption.create(
                        text = "aaa",
                        media = InputMedia.PollSticker(
                            media = Attachment.fileId("1"),
                            emoji = ":)"
                        )
                    )
                ),
                media = InputMedia.Photo(
                    media = Attachment.fileId("2")
                )
            ),
            skip = setOf(Companion.Mappers.TELEGRAM) // deserialization of attachment is not supported here
        )
        assertSerializable<SendPoll.Regular.Indefinite>(
            SendPoll.Regular.Indefinite(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf()
            )
        )
        assertSerializable<SendPoll>(
            SendPoll.Quiz.OpenPeriod(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                openPeriod = Duration.ofHours(1),
                correctOptionIds = listOf(1)
            )
        )
        assertSerializable<SendPoll.Quiz.OpenPeriod>(
            SendPoll.Quiz.OpenPeriod(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                openPeriod = Duration.ofHours(1),
                correctOptionIds = listOf(1)
            )
        )
        assertSerializable<SendPoll>(
            SendPoll.Quiz.CloseDate(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                closeDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                correctOptionIds = listOf(1)
            )
        )
        assertSerializable<SendPoll.Quiz.CloseDate>(
            SendPoll.Quiz.CloseDate(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                closeDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                correctOptionIds = listOf(1)
            )
        )
        assertSerializable<SendPoll>(
            SendPoll.Quiz.Indefinite(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                correctOptionIds = listOf(1),
                explanationMedia = InputMedia.LivePhoto(
                    media = Attachment.fileId("1"),
                    photo = Attachment.fileId("2")
                )
            ),
            skip = setOf(Companion.Mappers.TELEGRAM) // deserialization of attachment is not supported here
        )
        assertSerializable<SendPoll.Quiz.Indefinite>(
            SendPoll.Quiz.Indefinite(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                correctOptionIds = listOf(1)
            )
        )
    }

    @Test
    fun `setGameScore should survive serialization`() {
        assertSerializable<SetGameScore>(
            SetGameScore.InlineMessage(
                inlineMessageId = "1",
                userId = 1,
                score = 1
            )
        )
        assertSerializable<SetGameScore>(
            SetGameScore.ChatMessage(
                chatId = 1,
                messageId = 1,
                userId = 1,
                score = 1
            )
        )
    }

    @Test
    fun `stopMessageLiveLocation should survive serialization`() {
        assertSerializable<StopMessageLiveLocation>(
            StopMessageLiveLocation.InlineMessage(
                inlineMessageId = "1"
            )
        )
        assertSerializable<StopMessageLiveLocation>(
            StopMessageLiveLocation.ChatMessage(
                chatId = 1.toChatId(),
                messageId = 1,
            )
        )
    }
}
