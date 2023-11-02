package ski.gagar.vxutil.jooq

import io.vertx.core.Vertx
import io.vertx.core.WorkerExecutor
import io.vertx.core.impl.cpu.CpuCoreSensor
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.transactionResultInt
import org.jooq.tools.jdbc.JDBCUtils
import ski.gagar.vxutil.lazy
import ski.gagar.vxutil.logger
import ski.gagar.vxutil.workerExecutorDispatcher
import java.io.Closeable
import java.util.concurrent.Callable
import javax.sql.DataSource

typealias WorkerExecutorFactory = Vertx.(String) -> WorkerExecutor

val NO_ARGS_WORKER_EXECUTOR_FACTORY: WorkerExecutorFactory = { name -> createSharedWorkerExecutor(name) }
val DOUBLE_CPU_WORKER_EXECUTOR_FACTORY: WorkerExecutorFactory =
    { name -> createSharedWorkerExecutor(name, 2 * CpuCoreSensor.availableProcessors()) }


class Database(
    vertx: Vertx,
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

    suspend operator fun <T> invoke(body: suspend DSLContext.() -> T) = coroutineScope {
        withContext(dispatcher) {
            dsl.run { body() }
        }
    }

    override fun close() {
        executor.close()
    }

    inner class WithTransaction {
        suspend operator fun <T> invoke(body: suspend DSLContext.() -> T): T =
            dsl.transactionResultCoro { conf ->
                DSL.using(conf).body()
            }
    }

    companion object {
        const val DEFAULT_EXECUTOR_NAME = "ski.gagar.vxutil.jooq.dbExecutor"
        const val DEFAULT_DATA_SOURCE_NAME = "default"

        private suspend fun migrate(vertx: Vertx, ds: DataSource, locations: List<String>) {
            logger.lazy.info { "Running migrations for $ds" }
            vertx.executeBlocking(Callable<Unit> {
                Flyway.configure()
                    .dataSource(ds)
                    .locations(*locations.toTypedArray())
                    .load()
                    .migrate()
            }).await()
        }

        suspend fun initDs(vertx: Vertx, name: String, config: DbConfig, migrate: Boolean = true): DataSource {
            logger.lazy.info { "Creating shared data source with config $config" }
            val ds = vertx.createSharedDataSource(name, config.jdbcUrl, config.username, config.password)

            if (migrate) {
                migrate(vertx, ds, config.flywayLocations)
            }

            return ds
        }
        suspend fun initDs(vertx: Vertx, config: DbConfig, migrate: Boolean = true) {
            initDs(vertx, DEFAULT_DATA_SOURCE_NAME, config, migrate)
        }
        fun deleteDs(vertx: Vertx, name: String) {
            vertx.deleteSharedDataSource(name)
        }
    }
}

suspend fun Vertx.initDs(name: String, config: DbConfig) = Database.initDs(this, name, config)
suspend fun Vertx.initDs(config: DbConfig) = Database.initDs(this, config)
fun Vertx.deleteDs(name: String) = Database.deleteDs(this, name)

fun Vertx.Database(scope: CoroutineScope,
                   executorName: String = Database.DEFAULT_EXECUTOR_NAME,
                   dataSourceName: String = Database.DEFAULT_DATA_SOURCE_NAME) =
    Database(this, scope, executorName, dataSourceName)

fun CoroutineVerticle.Database(executorName: String = Database.DEFAULT_EXECUTOR_NAME,
                               dataSourceName: String = Database.DEFAULT_DATA_SOURCE_NAME) =
    Database(vertx, this, executorName, dataSourceName)

suspend fun <T> DSLContext.transactionResultCoro(block: suspend (Configuration) -> T): T =
    transactionResultInt(block)
