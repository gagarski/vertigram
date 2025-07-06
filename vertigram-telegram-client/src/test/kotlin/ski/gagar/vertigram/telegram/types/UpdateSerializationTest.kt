package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import java.time.Instant
import java.time.temporal.ChronoUnit

object UpdateSerializationTest : BaseSerializationTest() {
    @Test
    fun `update should survive serialization`() {
        assertSerializable<Update<*>>(
            Update.Message.create(
                updateId = 1,
                message = Message.create(
                    messageId = 1,
                    chat = Chat.create(
                        id = 1,
                        type = Chat.Type.PRIVATE
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.EditedMessage.create(
                updateId = 1,
                editedMessage = Message.create(
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
            Update.ChannelPost.create(
                updateId = 1,
                channelPost = Message.create(
                    messageId = 1,
                    chat = Chat.create(
                        id = 1,
                        type = Chat.Type.CHANNEL
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.EditedChannelPost.create(
                updateId = 1,
                editedChannelPost = Message.create(
                    messageId = 1,
                    chat = Chat.create(
                        id = 1,
                        type = Chat.Type.CHANNEL
                    ),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.MessageReaction.create(
                updateId = 1,
                messageReaction = Update.MessageReaction.Payload.create(
                    messageId = 1,
                    chat = Chat.create(
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
            Update.MessageReactionCount.create(
                updateId = 1,
                messageReactionCount = Update.MessageReactionCount.Payload.create(
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
            Update.InlineQuery.create(
                updateId = 1,
                inlineQuery = InlineQuery(
                    id = "1",
                    from = User.create(id = 1),
                    query = "aaa",
                    offset = "aaa",

                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChosenInlineResult.create(
                updateId = 1,
                chosenInlineResult = Update.ChosenInlineResult.Payload.create(
                    resultId = "1",
                    from = User.create(id = 1),
                    query = "aaa"
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.CallbackQuery.create(
                updateId = 1,
                callbackQuery = Update.CallbackQuery.Payload.create(
                    id = "1",
                    from = User.create(id = 1),
                    chatInstance = "1"
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ShippingQuery.create(
                updateId = 1,
                shippingQuery = Update.ShippingQuery.Payload.create(
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
            Update.PreCheckoutQuery.create(
                updateId = 1,
                preCheckoutQuery = Update.PreCheckoutQuery.Payload.create(
                    id = "1",
                    from = User.create(id = 1),
                    invoicePayload = "1",
                    currency = "USD",
                    totalAmount = 1
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.PurchasedPaidMedia.create(
                updateId = 1,
                purchasedPaidMedia = Update.PurchasedPaidMedia.Payload.create(
                    id = "1",
                    from = User.create(id = 1),
                    paidMediaPayload = "1"
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.Poll.create(
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
            Update.PollAnswer.create(
                updateId = 1,
                pollAnswer = Poll.Answer.create(
                    pollId = "1",
                    optionIds = listOf()
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.MyChatMember.create(
                updateId = 1,
                myChatMember = Update.ChatMember.Payload.create(
                    chat = Chat.create(id = 1, type = Chat.Type.SUPERGROUP),
                    from = User.create(id = 1),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    oldChatMember = ChatMember.Member.create(user = User.create(id = 1)),
                    newChatMember = ChatMember.Member.create(user = User.create(id = 1))
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChatMember.create(
                updateId = 1,
                chatMember = Update.ChatMember.Payload.create(
                    chat = Chat.create(id = 1, type = Chat.Type.SUPERGROUP),
                    from = User.create(id = 1),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    oldChatMember = ChatMember.Member.create(user = User(id = 1)),
                    newChatMember = ChatMember.Member.create(user = User(id = 1))
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChatJoinRequest(
                updateId = 1,
                chatJoinRequest = Update.ChatJoinRequest.Payload.create(
                    chat = Chat.create(id = 1, type = Chat.Type.SUPERGROUP),
                    from = User.create(id = 1),
                    date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    userChatId = 1
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.ChatBoost(
                updateId = 1,
                chatBoost = Update.ChatBoost.Payload.create(
                    chat = Chat.create(id = 1, type = Chat.Type.SUPERGROUP),
                    boost = ChatBoost.create(
                        boostId = "1",
                        addDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                        expirationDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                        source = ChatBoost.Source.Giveaway.create(
                            giveAwayMessageId = 1,
                            user = User(1)
                        )
                    )
                )
            )
        )
        assertSerializable<Update<*>>(
            Update.RemovedChatBoost.create(
                updateId = 1,
                chatBoostRemoved = Update.RemovedChatBoost.Payload.create(
                    chat = Chat.create(id = 1, type = Chat.Type.SUPERGROUP),
                    boostId = "1",
                    removeDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                    source = ChatBoost.Source.Giveaway.create(
                        giveAwayMessageId = 1,
                        user = User(1)
                    )
                )
            )
        )
    }

}