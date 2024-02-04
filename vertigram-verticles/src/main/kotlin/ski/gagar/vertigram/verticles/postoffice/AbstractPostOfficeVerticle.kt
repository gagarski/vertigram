package ski.gagar.vertigram.verticles.postoffice

import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.jackson.publishJson
import ski.gagar.vertigram.jackson.suspendJsonConsumer
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.verticles.address.VxUtilAddress
import ski.gagar.vertigram.verticles.children.AbstractHierarchyVerticle
import java.time.Duration
import java.time.Instant
import java.util.*

abstract class AbstractPostOfficeVerticle<
        M,
        D: AbstractPostOfficeVerticle.Discriminator,
        S : AbstractPostOfficeVerticle.SubscriptionInfo<D>> : AbstractHierarchyVerticle() {
    abstract val incomingAddress: String
    open val subscribeAddress: String
        get() = VxUtilAddress.Private.withClassifier(deploymentID, VxUtilAddress.PostOffice.Classifier.Subscribe)
    open val unsubscribeAddress: String
        get() = VxUtilAddress.Private.withClassifier(deploymentID, VxUtilAddress.PostOffice.Classifier.Unsubscribe)
    abstract val messageClass: Class<M>
    abstract val subInfoClass: Class<S>

    abstract val storagePeriod: Duration
    open val cleanupPeriod: Duration
        get() = storagePeriod.dividedBy(2)

    protected val stateMutex: Mutex = Mutex()

    private val mailboxes: MutableMap<D, MutableList<Envelope>> = mutableMapOf()
    private val subscriptions: MutableMap<D, MutableSet<S>> = mutableMapOf()
    private val receipts: MutableMap<String, MutableSet<Receipt>> = mutableMapOf()

    abstract fun discriminate(msg: M): D
    open suspend fun shouldAcceptMessage(msg: M): Boolean = true
    open suspend fun shouldAllowSubscribe(sub: S): Boolean = true
    open suspend fun shouldPassMessageToSubscriber(msg: M, sub: S): Boolean = true


    override suspend fun start() {
        vertx.setPeriodic(cleanupPeriod.toMillis()) {
            launch {
                cleanUp()
            }
        }

        suspendJsonConsumer(
            requestClass = messageClass,
            resultClass = Unit::class.java,
            address = incomingAddress,
            function = this::handleMessage
        )

        suspendJsonConsumer(
            requestClass = subInfoClass,
            resultClass = Unit::class.java,
            address = subscribeAddress,
            function = this::handleSubscribe
        )

        suspendJsonConsumer(
            requestClass = subInfoClass,
            resultClass = Unit::class.java,
            address = unsubscribeAddress,
            function = this::handleUnsubscribe
        )
    }

    private suspend fun handleMessage(msg: M) = messageHandler {
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

    private suspend fun handleSubscribe(subscriptionRequest: S) = messageHandler {
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

    private suspend fun handleUnsubscribe(subscriptionRequest: S) = messageHandler {
        stateMutex.withLock {
            val d = subscriptionRequest.discriminator
            unsubscribeSingle(subscriptionRequest, d)
        }
    }

    private fun unsubscribeSingle(req: SubscriptionInfo<D>, discriminator: D) {
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

    private suspend fun passAllEnvelopesToSubscriber(discriminator: Discriminator, subscription: S) {
        for (envelope in mailboxes[discriminator] ?: setOf()) {
            passEnvelopeToSubscriber(envelope, subscription)
        }
    }

    private suspend fun passEnvelopeToSubscriber(envelope: Envelope, subscription: S) {
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
        vertx.eventBus().publishJson(subscription.address, envelope.message)
        receipts.getOrPut(envelope.id) { mutableSetOf() }.add(envelope.receipt(subscription.address))
    }

    interface Discriminator

    interface SubscriptionInfo<D : Discriminator> {
        val address: String
        val discriminator: D
    }

    private data class Receipt(val id: String, val address: String)
    private inner class Envelope(val message: M, val arrivalDate: Instant = Instant.now(),
                                 val id: String  = UUID.randomUUID().toString()) {
        fun receipt(address: String) = Receipt(id, address)

        override fun toString(): String {
            return "Envelope(message=$message, arrivalDate=$arrivalDate, id='$id')"
        }
    }
}
