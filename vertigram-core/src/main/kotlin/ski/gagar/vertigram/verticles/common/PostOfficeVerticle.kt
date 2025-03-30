package ski.gagar.vertigram.verticles.common

import com.fasterxml.jackson.core.type.TypeReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.util.jackson.typeReference
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.common.PostOfficeVerticle.Discriminator
import ski.gagar.vertigram.verticles.common.PostOfficeVerticle.SubscriptionInfo
import ski.gagar.vertigram.verticles.common.address.VertigramCommonAddress
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * A Verticle that introduce a bit of very dumb persistence to [ski.gagar.vertigram.Vertigram.EventBus].
 *
 * It allows to store messages coming to [incomingAddress] for subscribers which are selected by [Discriminator] before
 * the subscribers have subscribed for these messages.
 *
 * When message arrives at [incomingAddress]:
 *  - [Discriminator] is evaluated using [discriminate] function (implemented by subclasses)
 *  - All the active subscribers for discriminator receive a copy of the message to the address, specified in
 *    [SubscriptionInfo]
 *  - The message is stored in per-discriminator mailbox for [storagePeriod]
 *
 *  When message arrives at [subscribeAddress]:
 *  - All the messages stored for the [SubscriptionInfo.discriminator] are being immediately published to the
 *    new subscriber address (given in [SubscriptionInfo]
 *  - New subscriber will receive new messages with given discriminator to his address as they arrive
 *
 *  When message arrives at [unsubscribeAddress]
 *  - [PostOfficeVerticle] stops publishing messages to the subscriber as they arrive
 *  - Nothing is removed from the mailboxes
 *
 *  [PostOfficeVerticle] tracks forward receipts, meaning if the subscriber unsibscribes and then subscribes
 *  again using the same address (only addresses are used to identify subscribers), [PostOfficeVerticle]
 *  will track which of the stored messages the subscriber has already received and won't resend them.
 *
 *  @param Config Configuration type
 *  @param Message Message type
 *  @param Discriminator Discriminator to get a receiver by message
 *  @param SubscriptionInfo Subscription info message type
 */
abstract class PostOfficeVerticle<
        Config,
        Message,
        Discriminator: PostOfficeVerticle.Discriminator,
        SubscriptionInfo : PostOfficeVerticle.SubscriptionInfo<Discriminator>> : HierarchyVerticle<Config>() {
    /**
     * Incoming messages address, should be overridden by subclasses.
     */
    abstract val incomingAddress: String

    /**
     * Subscribe request address, should be overridden by subclasses.
     */
    open val subscribeAddress: String
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, VertigramCommonAddress.PostOffice.Classifier.Subscribe)
    /**
     * Unsubscribe request address, should be overridden by subclasses.
     */
    open val unsubscribeAddress: String
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, VertigramCommonAddress.PostOffice.Classifier.Unsubscribe)

    /**
     * [TypeReference] for [Message] type.
     *
     * Should be overriden by subclasses, in most cases it's enough to just call [typeReference]
     */
    abstract val messageTypeRef: TypeReference<Message>

    /**
     * [TypeReference] for [SubscriptionInfo] type.
     *
     * Should be overriden by subclasses, in most cases it's enough to just call [typeReference]
     */
    abstract val subInfoTypeRef: TypeReference<SubscriptionInfo>

    /**
     * Storage period before [Message]s are dropped, should be overridden by subclasses.
     */
    abstract val storagePeriod: Duration

    /**
     * Period for expired messages cleanup task, may be overridden by subclasses, by default [storagePeriod] / 2.
     */
    open val cleanupPeriod: Duration
        get() = storagePeriod.dividedBy(2)

    /**
     * Mutex that guards the internal state
     */
    protected val stateMutex: Mutex = Mutex()

    private val mailboxes: MutableMap<Discriminator, MutableList<Envelope>> = mutableMapOf()
    private val subscriptions: MutableMap<Discriminator, MutableSet<SubscriptionInfo>> = mutableMapOf()
    private val receipts: MutableMap<String, MutableSet<Receipt>> = mutableMapOf()

    /**
     * Get [Discriminator] for [Message], should be overridden by subclasses.
     */
    abstract fun discriminate(msg: Message): Discriminator

    /**
     * Should verticle accept the [msg]? Can be overriden by subclasses. By default, all messages are accepted.
     */
    open suspend fun shouldAcceptMessage(msg: Message): Boolean = true

    /**
     * Should verticle accept the [subRequest]? Can be overriden by subclasses. By default, all subscription requests are accepted.
     */
    open suspend fun shouldAllowSubscribe(subRequest: SubscriptionInfo): Boolean = true

    /**
     * Should [msg] be forwarded to given [subInfo]. Can be overriden by subclasses.
     *
     * Can be used to do per-message filtering. By default, all messages are forwarded to all subscribers.
     */
    open suspend fun shouldPassMessageToSubscriber(msg: Message, subInfo: SubscriptionInfo): Boolean = true


    override suspend fun start() {
        vertx.setPeriodic(cleanupPeriod.toMillis()) {
            launch {
                cleanUp()
            }
        }

        consumerNonReified(
            requestJavaType = vertigram.objectMapper.constructType(messageTypeRef.type),
            address = incomingAddress,
            function = this::handleMessage
        )

        consumerNonReified(
            requestJavaType = vertigram.objectMapper.constructType(subInfoTypeRef.type),
            address = subscribeAddress,
            function = this::handleSubscribe
        )

        consumerNonReified(
            requestJavaType = vertigram.objectMapper.constructType(subInfoTypeRef.type),
            address = unsubscribeAddress,
            function = this::handleUnsubscribe
        )
    }

    private suspend fun handleMessage(msg: Message) = messageHandler {
        stateMutex.withLock {
            logger.lazy.debug {
                "Post office $name got message $msg"
            }
            if (!shouldAcceptMessage(msg)) {
                return@messageHandler
            }

            logger.lazy.debug {
                "Post office $name accepted message $msg"
            }

            val discriminator = discriminate(msg)

            val env = Envelope(msg)
            mailboxes.getOrPut(discriminator) { mutableListOf() }.add(env)

            passEnvelopeToAllSubscribers(discriminator, env)
        }
    }

    private suspend fun handleSubscribe(subscriptionRequest: SubscriptionInfo) = messageHandler {
        stateMutex.withLock {
            val discriminator = subscriptionRequest.discriminator
            logger.lazy.debug {
                "Post office $name got subscription request $subscriptionRequest"
            }
            if (!shouldAllowSubscribe(subscriptionRequest)) {
                return@messageHandler
            }

            logger.lazy.debug {
                "Post office $name accepted subscription request $subscriptionRequest"
            }


            subscriptions.getOrPut(discriminator) { mutableSetOf() }.add(subscriptionRequest)

            passAllEnvelopesToSubscriber(discriminator, subscriptionRequest)
        }
    }

    private suspend fun handleUnsubscribe(subscriptionRequest: SubscriptionInfo) = messageHandler {
        stateMutex.withLock {
            val d = subscriptionRequest.discriminator
            unsubscribeSingle(subscriptionRequest, d)
        }
    }

    private fun unsubscribeSingle(req: PostOfficeVerticle.SubscriptionInfo<Discriminator>, discriminator: Discriminator) {
        (subscriptions[discriminator] ?: mutableSetOf()).remove(req)
    }

    private fun cleanUp() {
        logger.lazy.debug {
            "Started clean up in post office $name"
        }
        val retentionDate = Instant.now() - storagePeriod
        val boxIter = mailboxes.iterator()

        while (boxIter.hasNext()) {
            val (_, box) = boxIter.next()
            val envIter = box.iterator()

            while (envIter.hasNext()) {
                val env = envIter.next()

                if (env.arrivalDate < retentionDate) {
                    logger.lazy.debug {
                        "Post office $name has $env thrown off due to its expiration"
                    }
                    envIter.remove()
                    receipts.remove(env.id)
                }
            }

            if (box.isEmpty()) {
                boxIter.remove()
            }
        }
    }

    private suspend fun passEnvelopeToAllSubscribers(discriminator: Discriminator, envelope: Envelope) {
        for (subscription in subscriptions[discriminator] ?: setOf()) {
            passEnvelopeToSubscriber(envelope, subscription)
        }
    }

    private suspend fun passAllEnvelopesToSubscriber(discriminator: Discriminator, subscription: SubscriptionInfo) {
        for (envelope in mailboxes[discriminator] ?: setOf()) {
            passEnvelopeToSubscriber(envelope, subscription)
        }
    }

    private suspend fun passEnvelopeToSubscriber(envelope: Envelope, subscription: SubscriptionInfo) {
        if (envelope.receipt(subscription.address) in (receipts[envelope.id] ?: setOf())) {
            logger.lazy.debug {
                "Post office $name is skipping passing $envelope to $subscription because it has already received it before"
            }
            return
        }

        if (!shouldPassMessageToSubscriber(envelope.message, subscription)) {
            logger.lazy.debug {
                "Post office $name is skipping passing $envelope to $subscription because its not eligible to receive it"
            }
            return
        }
        logger.lazy.debug {
            "Passing $envelope to $subscription"
        }
        vertigram.eventBus.publish(subscription.address, envelope.message)
        receipts.getOrPut(envelope.id) { mutableSetOf() }.add(envelope.receipt(subscription.address))
    }

    /**
     * An interface to be implemented by discriminator
     */
    interface Discriminator

    /**
     * An interface to be implemented by subscription info
     */
    interface SubscriptionInfo<D : Discriminator> {
        val address: String
        val discriminator: D
    }

    private data class Receipt(val id: String, val address: String)
    private inner class Envelope(val message: Message, val arrivalDate: Instant = Instant.now(),
                                 val id: String  = UUID.randomUUID().toString()) {
        fun receipt(address: String) = Receipt(id, address)

        override fun toString(): String {
            return "Envelope(message=$message, arrivalDate=$arrivalDate, id='$id')"
        }
    }
}
