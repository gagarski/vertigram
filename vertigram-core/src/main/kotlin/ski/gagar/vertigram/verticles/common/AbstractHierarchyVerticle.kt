package ski.gagar.vertigram.verticles.common

import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason

/**
 * A verticle that introduces a concept of verticle hierarchy.
 *
 * [AbstractHierarchyVerticle] can have children, spawned by [deployChild]ю
 *
 * The main traits of [AbstractHierarchyVerticle] are;
 *  - it can deploy children
 *  - it has lifecycle, specifically it can optionally [die]
 *  - parents and children are notified on each other death and can act accordingly, based on death reason
 *  - parents and children can optionally talk to each other using private addresses based on [deploymentID]
 */
abstract class AbstractHierarchyVerticle<Config> : VertigramVerticle<Config>() {
    private val children = mutableSetOf<String>()
    private var deathReason: DeathReason? = null

    /**
     * Is the verticle already dead
     */
    protected val isDead
        get() = deathReason != null

    override suspend fun start() {
        logger.lazy.debug {
            "$name: adding handleDeathNotice handler on $DEATH_NOTICE_ADDRESS"
        }
        consumer(DEATH_NOTICE_ADDRESS, function = ::handleDeathNotice)
        logger.lazy.debug {
            "$name: adding handleParentDeathNotice handler on ${parentDeathNoticeAddress(deploymentID)}"
        }
        consumer(parentDeathNoticeAddress(deploymentID), function = ::handleParentDeathNotice)
    }


    override suspend fun stop() {
        val notice = DeathNotice(deploymentID, deathReason ?: DeathReason.FAILED)
        logger.lazy.debug {
            "$name: publishing $notice to $DEATH_NOTICE_ADDRESS"
        }
        vertigram.eventBus.publish(DEATH_NOTICE_ADDRESS, notice)
        for (child in children) {
            logger.lazy.debug {
                "$name: publishing $notice to ${parentDeathNoticeAddress(child)}"
            }
            vertigram.eventBus.publish(parentDeathNoticeAddress(child), notice)
        }
    }

    /**
     * Action on parent's death.
     *
     * Can be overridden. By default, die with the same reason as parent.
     */
    protected open fun onParentDeath(
        /**
         * Death notice message
         */
        deathNotice: DeathNotice
    ) {
        die(deathNotice.reason)
    }

    /**
     * Action on child death.
     *
     * Can be overridden, by default, do nothing.
     */
    protected open suspend fun onChildDeath(
        /**
         * Death notice message
         */
        deathNotice: DeathNotice
    ) {}

    /**
     * A callback to be called before death.
     */
    protected open fun beforeDeath(
        /**
         * Future death reason
         */
        reason: DeathReason
    ) {}

    /**
     * Deploy a child [verticle] using [config].
     */
    protected suspend fun <T> deployChild(verticle: VertigramVerticle<T>,
                                          config: T): String {
        val id = vertigram.deployVerticle(verticle, Vertigram.DeploymentOptions(vertigram, config))
        children.add(id)
        return id
    }

    /**
     * Deploy non-configurable child [verticle]
     */
    protected suspend fun deployChild(verticle: VertigramVerticle<Unit?>): String {
        val id = vertigram.deployVerticle(verticle)
        children.add(id)
        return id
    }

    /**
     * Die with given [reason].
     *
     * Dying means calling [beforeDeath], undeploying the verticle and publishing event bus notification about death.
     *
     * For notification [DeathNotice] object is published to two kinds of addresses:
     *  - Global [DEATH_NOTICE_ADDRESS] to notify parent verticle about death
     *    (it will be listening for this address and track deaths of its children)
     *  - Per-child address [parentDeathNoticeAddress] which will notify each child separately on parent death.
     */
    protected fun die(reason: DeathReason) {
        vertx.runOnContext {
            // Putting it to the event queue so that we can die during start() (otherwise undeploy dos not work)
            if (null != deathReason)
                throw IllegalStateException("$name already stopped with reason $deathReason")

            deathReason = reason
            beforeDeath(reason)
            vertx.undeploy(deploymentID)
        }
    }

    /**
     * Die as completed.
     */
    protected fun complete() {
        die(DeathReason.COMPLETED)
    }

    /**
     * Die as failed.
     */
    protected fun fail() {
        die(DeathReason.FAILED)
    }

    /**
     * Die as cancelled.
     */
    protected fun cancel() {
        die(DeathReason.CANCELLED)
    }

    /**
     * Die as timed out.
     */
    protected fun timeout() {
        die(DeathReason.TIMEOUT)
    }

    /**
     * Convenience method to wrap message handler for [consumer].
     *
     * Besides executing the [block] performs state management: ignores the message if for some reason
     * it arrived after death and [fail]s the verticle if the exception happened.
     */
    protected suspend fun messageHandler(block: suspend () -> Unit) {
        try {
            if (isDead) return
            block()
        } catch (t: Throwable) {
            logger.lazy.error(throwable = t) { "Failing verticle due to exception" }
            fail()
        }
    }

    private suspend fun handleDeathNotice(deathNotice: DeathNotice) {
        if (isDead) return
        if (deathNotice.id !in children) return
        logger.lazy.debug {
            "$name got $deathNotice from child"
        }
        children.remove(deathNotice.id)
        onChildDeath(deathNotice)
    }

    private fun handleParentDeathNotice(deathNotice: DeathNotice) {
        if (isDead) return
        logger.lazy.debug {
            "$name got $deathNotice from parent"
        }
        onParentDeath(deathNotice)
    }


    companion object {
        const val DEATH_NOTICE_ADDRESS = "ski.gagar.deathNotice"
        fun parentDeathNoticeAddress(childId: String) = "ski.gagar.deathNotice.parent.${childId}"
    }

}

