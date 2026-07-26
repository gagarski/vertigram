package ski.gagar.vertigram.verticles.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.awaitRegistration
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason

/**
 * A verticle that introduces a concept of verticle hierarchy.
 *
 * [HierarchyVerticle] can have children, spawned by [deployChild].
 *
 * The main traits of [HierarchyVerticle] are;
 *  - it can deploy children
 *  - it has lifecycle, specifically it can optionally [die]
 *  - parents are notified when their children die and can act accordingly, based on death reason
 *  - parents and children can optionally talk to each other using private addresses based on [deploymentID]
 */
abstract class HierarchyVerticle<Config> : VertigramVerticle<Config>() {
    private var deathReason: DeathReason? = null
    private val parentChildDeathNoticeAddress: String?
        get() = config
            .getJsonObject(InternalDeploymentMetadata.FIELD)
            ?.getString(InternalDeploymentMetadata.PARENT_CHILD_DEATH_NOTICE_ADDRESS)

    /**
     * Is the verticle already dead
     */
    protected val isDead
        get() = deathReason != null

    override suspend fun start() {
        super.start()

        logger.lazy.debug {
            "$name: adding handleChildDeathNotice handler on ${childDeathNoticeAddress(deploymentID)}"
        }
        consumer(
            childDeathNoticeAddress(deploymentID),
            function = ::handleChildDeathNotice
        ).awaitRegistration()
    }


    override suspend fun stop() {
        val notice = DeathNotice(deploymentID, deathReason ?: DeathReason.FAILED)

        parentChildDeathNoticeAddress?.let { address ->
            logger.lazy.debug {
                "$name: publishing $notice to $address"
            }
            vertigram.eventBus.publish(address, notice)
        }
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
     * Deploy a child [verticle] using [config].
     */
    protected suspend fun <T> deployChild(verticle: VertigramVerticle<T>,
                                          config: T): String {
        val id = vertigram.deployVerticle(
            verticle,
            Vertigram.DeploymentOptions.withInternalMetadata(
                vertigram = vertigram,
                config = config,
                metadata = mapOf(
                    InternalDeploymentMetadata.PARENT_CHILD_DEATH_NOTICE_ADDRESS to
                            childDeathNoticeAddress(deploymentID)
                )
            )
        )
        return id
    }

    /**
     * Deploy non-configurable child [verticle]
     */
    protected suspend fun deployChild(verticle: VertigramVerticle<Unit?>): String {
        val id = vertigram.deployVerticle(
            verticle,
            Vertigram.DeploymentOptions.withInternalMetadata<Unit?>(
                vertigram = vertigram,
                config = null,
                metadata = mapOf(
                    InternalDeploymentMetadata.PARENT_CHILD_DEATH_NOTICE_ADDRESS to
                            childDeathNoticeAddress(deploymentID)
                )
            )
        )
        return id
    }

    /**
     * Die with given [reason].
     *
     * Dying means undeploying the verticle. Vert.x automatically undeploys child deployments first.
     * During [stop], a [DeathNotice] is published to the private parent callback address passed in
     * deployment metadata.
     */
    protected fun die(reason: DeathReason) {
        if (deathReason != null) return
        deathReason = reason

        vertx.runOnContext {
            // Putting it to the event queue so that we can die during start() (otherwise undeploy dos not work)
            undeploySafely()
        }
    }

    private fun undeploySafely() {
        CoroutineScope(coroutineContext + SupervisorJob()).launch {
            try {
                vertigram.undeploy(deploymentID)
            } catch (t: Throwable) {
                logger.lazy.error(throwable = t) {
                    "$name: failed to undeploy"
                }
            }
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

    private suspend fun handleChildDeathNotice(deathNotice: DeathNotice) {
        if (isDead) return

        logger.lazy.debug {
            "$name got $deathNotice from child"
        }
        onChildDeath(deathNotice)
    }

    companion object {
        fun childDeathNoticeAddress(parentId: String) = "ski.gagar.deathNotice.child.${parentId}"
    }

}

