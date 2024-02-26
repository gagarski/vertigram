package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import java.time.Instant
import java.time.temporal.ChronoUnit

object UpdateSerializationTest : BaseSerializationTest() {
    @Test
    fun `update should survive serialization`() {
        assertSerializable<Update<*>>(
            Update.Message(
                updateId = 1,
                message = Message(
                    messageId = 1,
                    chat = Chat(
                        id = 1,
                        type = Chat.Type.PRIVATE
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.EditedMessage(
                updateId = 1,
                editedMessage = Message(
                    messageId = 1,
                    chat = Chat(
                        id = 1,
                        type = Chat.Type.PRIVATE
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChannelPost(
                updateId = 1,
                channelPost = Message(
                    messageId = 1,
                    chat = Chat(
                        id = 1,
                        type = Chat.Type.CHANNEL
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.EditedChannelPost(
                updateId = 1,
                editedChannelPost = Message(
                    messageId = 1,
                    chat = Chat(
                        id = 1,
                        type = Chat.Type.CHANNEL
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.MessageReaction(
                updateId = 1,
                messageReaction = Update.MessageReaction.Payload(
                    messageId = 1,
                    chat = Chat(
                        id = 1,
                        type = Chat.Type.CHANNEL
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    oldReaction = listOf(),
                    newReaction = listOf()
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.MessageReactionCount(
                updateId = 1,
                messageReactionCount = Update.MessageReactionCount.Payload(
                    messageId = 1,
                    chat = Chat(
                        id = 1,
                        type = Chat.Type.CHANNEL
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    reactions = listOf()
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.InlineQuery(
                updateId = 1,
                inlineQuery = InlineQuery(
                    id = "1",
                    from = User(id = 1),
                    query = "aaa",
                    offset = "aaa",

                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChosenInlineResult(
                updateId = 1,
                chosenInlineResult = Update.ChosenInlineResult.Payload(
                    resultId = "1",
                    from = User(id = 1),
                    query = "aaa"
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.CallbackQuery(
                updateId = 1,
                callbackQuery = Update.CallbackQuery.Payload(
                    id = "1",
                    from = User(id = 1),
                    chatInstance = "1"
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ShippingQuery(
                updateId = 1,
                shippingQuery = Update.ShippingQuery.Payload(
                    id = "1",
                    from = User(id = 1),
                    invoicePayload = "1",
                    shippingAddress = ShippingAddress(
                        countryCode = "DE",
                        state = "AAA",
                        city = "AAA",
                        postCode = "111",
                        streetLine1 = "111",
                        streetLine2 = "111"
                    )
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.PreCheckoutQuery(
                updateId = 1,
                preCheckoutQuery = Update.PreCheckoutQuery.Payload(
                    id = "1",
                    from = User(id = 1),
                    invoicePayload = "1",
                    currency = "USD",
                    totalAmount = 1
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.Poll(
                updateId = 1,
                poll = Poll.Regular(
                    id = "1",
                    question = "aaa",
                    options = listOf(),
                    totalVoterCount = 1
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.PollAnswer(
                updateId = 1,
                pollAnswer = Poll.Answer(
                    pollId = "1",
                    optionIds = listOf()
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.MyChatMember(
                updateId = 1,
                myChatMember = Update.ChatMember.Payload(
                    chat = Chat(id = 1, type = Chat.Type.SUPERGROUP),
                    from = User(id = 1),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    oldChatMember = ChatMember.Member(user = User(id = 1)),
                    newChatMember = ChatMember.Member(user = User(id = 1))
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChatMember(
                updateId = 1,
                chatMember = Update.ChatMember.Payload(
                    chat = Chat(id = 1, type = Chat.Type.SUPERGROUP),
                    from = User(id = 1),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    oldChatMember = ChatMember.Member(user = User(id = 1)),
                    newChatMember = ChatMember.Member(user = User(id = 1))
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChatJoinRequest(
                updateId = 1,
                chatJoinRequest = Update.ChatJoinRequest.Payload(
                    chat = Chat(id = 1, type = Chat.Type.SUPERGROUP),
                    from = User(id = 1),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    userChatId = 1
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChatBoost(
                updateId = 1,
                chatBoost = Update.ChatBoost.Payload(
                    chat = Chat(id = 1, type = Chat.Type.SUPERGROUP),
                    boost = ChatBoost(
                        boostId = "1",
                        addDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                        expirationDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                        source = ChatBoost.Source.Giveaway(
                            giveAwayMessageId = 1,
                            user = User(1)
                        )
                    )
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.RemovedChatBoost(
                updateId = 1,
                chatBoostRemoved = Update.RemovedChatBoost.Payload(
                    chat = Chat(id = 1, type = Chat.Type.SUPERGROUP),
                    boostId = "1",
                    removeDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    source = ChatBoost.Source.Giveaway(
                        giveAwayMessageId = 1,
                        user = User(1)
                    )
                )
            )
        )
    }

}