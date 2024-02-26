package ski.gagar.vertigram.verticles.common

import com.fasterxml.jackson.core.type.TypeReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.verticles.common.address.VertigramCommonAddress
import java.time.Duration
import java.time.Instant
import java.util.*

abstract class AbstractPostOfficeVerticle<
        Config,
        Message,
        Discriminator: AbstractPostOfficeVerticle.Discriminator,
        SubscriptionInfo : AbstractPostOfficeVerticle.SubscriptionInfo<Discriminator>> : AbstractHierarchyVerticle<Config>() {
    abstract val incomingAddress: String
    open val subscribeAddress: String
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, VertigramCommonAddress.PostOffice.Classifier.Subscribe)
    open val unsubscribeAddress: String
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, VertigramCommonAddress.PostOffice.Classifier.Unsubscribe)
    abstract val messageTypeRef: TypeReference<Message>
    abstract val subInfoTypeRef: TypeReference<SubscriptionInfo>

    abstract val storagePeriod: Duration
    open val cleanupPeriod: Duration
        get() = storagePeriod.dividedBy(2)

    protected val stateMutex: Mutex = Mutex()

    private val mailboxes: MutableMap<Discriminator, MutableList<Envelope>> = mutableMapOf()
    private val subscriptions: MutableMap<Discriminator, MutableSet<SubscriptionInfo>> = mutableMapOf()
    private val receipts: MutableMap<String, MutableSet<Receipt>> = mutableMapOf()

    abstract fun discriminate(msg: Message): Discriminator
    open suspend fun shouldAcceptMessage(msg: Message): Boolean = true
    open suspend fun shouldAllowSubscribe(sub: SubscriptionInfo): Boolean = true
    open suspend fun shouldPassMessageToSubscriber(msg: Message, sub: SubscriptionInfo): Boolean = true


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

    private fun unsubscribeSingle(req: AbstractPostOfficeVerticle.SubscriptionInfo<Discriminator>, discriminator: Discriminator) {
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

    interface Discriminator

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
