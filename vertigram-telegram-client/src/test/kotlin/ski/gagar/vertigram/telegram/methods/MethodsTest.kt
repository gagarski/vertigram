package ski.gagar.vertigram.telegram.methods

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.attachments.fileId
import ski.gagar.vertigram.telegram.types.methods.*
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
                text = "aaa"
            )
        )
        assertSerializable<EditMessageText>(
            EditMessageText.ChatMessage(
                chatId = 1.toChatId(),
                messageId = 1,
                text = "aaa"
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
    fun `sendPoll and sendQuiz should survive serialization`() {
        assertSerializable<SendPoll>(
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
        assertSerializable<SendPoll>(
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
                correctOptionId = 1
            )
        )
        assertSerializable<SendPoll>(
            SendPoll.Quiz.CloseDate(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                closeDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                correctOptionId = 1
            )
        )
        assertSerializable<SendPoll>(
            SendPoll.Quiz.Indefinite(
                chatId = 1.toChatId(),
                question = "aaa",
                options = listOf(),
                correctOptionId = 1
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