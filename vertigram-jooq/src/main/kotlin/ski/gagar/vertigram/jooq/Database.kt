package ski.gagar.vertigram.jooq

import io.vertx.core.Vertx
import io.vertx.core.WorkerExecutor
import io.vertx.core.impl.cpu.CpuCoreSensor
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.transactionResultInt
import org.jooq.tools.jdbc.JDBCUtils
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.util.workerExecutorDispatcher
import java.io.Closeable
import java.util.concurrent.Callable
import javax.sql.DataSource

typealias WorkerExecutorFactory = Vertx.(String) -> WorkerExecutor

/**
 * [WorkerExecutorFactory] that calls [Vertx.createSharedWorkerExecutor] without args
 */
val NO_ARGS_WORKER_EXECUTOR_FACTORY: WorkerExecutorFactory = { name -> createSharedWorkerExecutor(name) }

/**
 * [WorkerExecutorFactory] that creates an executor with number of threads equal to double number of CPUs
 */
val DOUBLE_CPU_WORKER_EXECUTOR_FACTORY: WorkerExecutorFactory =
    { name -> createSharedWorkerExecutor(name, 2 * CpuCoreSensor.availableProcessors()) }


/**
 * A wrapper for [Database] connection allowing to execute jOOQ queries inside Vertx Verticles.
 *
 * @param vertx [Vertx] instance
 * @param executorName A name for the [WorkerExecutor] on which the queries will be executed
 * @param dataSourceName Data source name
 * @param workerExecutorFactory [WorkerExecutorFactory] to use
 *
 * @sample ski.gagar.vertigram.samples.dbExample
 */
class Database(
    vertx: Vertx,
    /**
     * Coroutine scope (e.g. a verticle)
     */
    private val scope: CoroutineScope,
    executorName: String = DEFAULT_EXECUTOR_NAME,
    dataSourceName: String = DEFAULT_DATA_SOURCE_NAME,
    workerExecutorFactory: WorkerExecutorFactory = DOUBLE_CPU_WORKER_EXECUTOR_FACTORY
) : Closeable, CoroutineScope by scope {

    private val executor = vertx.workerExecutorFactory(executorName)
    private val dispatcher = vertx.workerExecutorDispatcher(executor)

    private val dsl: DSLContext = vertx.getSharedDataSource(dataSourceName).let {
        DSL.using(it, JDBCUtils.dialect(it.jdbcUrl))
    }

    val withTransaction = WithTransaction()

    /**
     * Run [body] with jOOQ query.
     *
     * @sample ski.gagar.vertigram.samples.dbExample
     */
    suspend operator fun <T> invoke(body: suspend DSLContext.() -> T) = coroutineScope {
        withContext(dispatcher) {
            dsl.run { body() }
        }
    }

    override fun close() {
        executor.close()
    }

    /**
     * Transaction wrapper
     *
     * @sample ski.gagar.vertigram.samples.dbTranExample
     */
    inner class WithTransaction {
        suspend operator fun <T> invoke(body: suspend DSLContext.() -> T): T =
            dsl.transactionResultCoro { conf ->
                DSL.using(conf).body()
            }
    }

    companion object {
        const val DEFAULT_EXECUTOR_NAME = "ski.gagar.vertigram.jooq.dbExecutor"
        const val DEFAULT_DATA_SOURCE_NAME = "default"

        private suspend fun migrate(vertx: Vertx, ds: DataSource, locations: List<String>) {
            logger.lazy.info { "Running migrations for $ds" }
            vertx.executeBlocking(Callable<Unit> {
                Flyway.configure()
                    .dataSource(ds)
                    .locations(*locations.toTypedArray())
                    .load()
                    .migrate()
            }).coAwait()
        }

        /**
         * Create data source with [name] on [vertx] from [config]
         *
         * @param vertx [Vertx] instance
         * @param config Data source config
         * @param name Data source name
         * @param migrate RunFlyway migrations
         */
        suspend fun attachDatasource(
            vertx: Vertx,
            config: DataSourceConfig,
            name: String = DEFAULT_DATA_SOURCE_NAME,
            migrate: Boolean = true
        ): DataSource {
            logger.lazy.info { "Creating shared data source with config $config" }
            val ds = vertx.createSharedDataSource(name, config.jdbcUrl, config.username, config.password)

            if (migrate) {
                migrate(vertx, ds, config.flywayLocations)
            }

            return ds
        }

        /**
         * Detach data source with [name] from [vertx]
         */
        fun detachDatasource(vertx: Vertx, name: String) {
            vertx.deleteSharedDataSource(name)
        }
    }
}

/**
 * Create data source with [name] on [this] from [config]
 */
suspend fun Vertx.attachDatasource(config: DataSourceConfig, name: String = Database.DEFAULT_DATA_SOURCE_NAME) = Database.attachDatasource(this, config, name)

/**
 * Detach data source with [name] from [this]
 */
fun Vertx.detachDatasource(name: String) = Database.detachDatasource(this, name)

/**
 * Create a [Database] object attached to [this] and [scope]
 */
fun Vertx.Database(scope: CoroutineScope,
                   executorName: String = Database.DEFAULT_EXECUTOR_NAME,
                   dataSourceName: String = Database.DEFAULT_DATA_SOURCE_NAME
) =
    Database(this, scope, executorName, dataSourceName)

/**
 * Create a [Database] object attached to [this]
 */
fun CoroutineVerticle.Database(executorName: String = Database.DEFAULT_EXECUTOR_NAME,
                               dataSourceName: String = Database.DEFAULT_DATA_SOURCE_NAME
) =
    Database(vertx, this, executorName, dataSourceName)

internal suspend fun <T> DSLContext.transactionResultCoro(block: suspend (Configuration) -> T): T =
    transactionResultInt(block)

