package ski.gagar.vertigram.verticles.common

import io.vertx.core.DeploymentOptions
import io.vertx.kotlin.coroutines.coAwait
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason
import java.util.*

abstract class AbstractHierarchyVerticle<Config> : VertigramVerticle<Config>() {
    private val children = mutableSetOf<String>()
    private var deathReason: DeathReason? = null

    protected val isDead
        get() = deathReason != null

    // Verticle lifecycle methods
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

    // Callbacks that can be overriden

    protected open fun onParentDeath(deathNotice: DeathNotice) {
        die(deathNotice.reason)
    }

    protected open fun onChildDeath(deathNotice: DeathNotice) {}


    protected open fun beforeDeath(reason: DeathReason) {}

    protected suspend fun deployChild(verticle: VertigramVerticle<*>,
                                      deploymentOptions: DeploymentOptions = DeploymentOptions()): String {
        val id = vertx.deployVerticle(verticle, deploymentOptions).coAwait()
        children.add(id)
        return id
    }

    // Can be called by implementors

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

    protected fun complete() {
        die(DeathReason.COMPLETED)
    }

    protected fun fail() {
        die(DeathReason.FAILED)
    }

    protected fun cancel() {
        die(DeathReason.CANCELLED)
    }

    protected fun timeout() {
        die(DeathReason.TIMEOUT)
    }

    protected suspend fun messageHandler(block: suspend () -> Unit) {
        try {
            if (isDead) return
            block()
        } catch (t: Throwable) {
            logger.lazy.error(throwable = t) { "Failing verticle due to exception" }
            fail()
        }
    }

    // Private stuff

    private fun handleDeathNotice(deathNotice: DeathNotice) {
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
        private fun nextId() = "${UUID.randomUUID()}"
    }

}

