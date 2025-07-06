package ski.gagar.vertigram.jooq

import com.zaxxer.hikari.HikariDataSource
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.shareddata.Shareable
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.runBlocking
import org.jooq.Record
import org.jooq.impl.DSL
import javax.sql.DataSource

data class ShareableHolder<T>(val data: T) : Shareable

class DataSourceWithUrl(private val ds: DataSource, val jdbcUrl: String) : DataSource by ds

const val DATA_SOURCES_MAP_NAME = "ski.gagar.vertigram.jooq.ds"

internal fun Vertx.createSharedDataSource(name: String, jdbcUrl: String, username: String? = null, password: String? = null): DataSource {
    val ds = HikariDataSource().apply dsConstruct@ {
        this.jdbcUrl = jdbcUrl
        username?.let {
            this@dsConstruct.username = username
        }
        password?.let {
            this@dsConstruct.password = password
        }

    }.let { DataSourceWithUrl(it, jdbcUrl) }

    sharedData().getLocalMap<String, ShareableHolder<DataSourceWithUrl>>(DATA_SOURCES_MAP_NAME)
        .compute(name) { _, old ->
            if (old != null)
                throw IllegalStateException("The data source with this name is already present")

            ShareableHolder(ds)
        }
    return ds
}

internal fun Vertx.getSharedDataSource(name: String): DataSourceWithUrl =
    sharedData().getLocalMap<String, ShareableHolder<DataSourceWithUrl>>(DATA_SOURCES_MAP_NAME)[name]?.data ?:
    throw IllegalArgumentException( "Shared data source $name is not present")

internal fun Vertx.deleteSharedDataSource(name: String) {
    sharedData().getLocalMap<String, ShareableHolder<DataSourceWithUrl>>(DATA_SOURCES_MAP_NAME)
        .compute(name) { _, old ->
            if (old != null)
                throw IllegalStateException("The data source with this name is already present")

            null
        }
}

fun main() {
    val vertx = Vertx.vertx()

    runBlocking(vertx.dispatcher()) {
        vertx.createSharedDataSource(
            name = "myapp",
            jdbcUrl = "jdbc:postgresql://localhost/myapp",
            username = "myapp",
            password = "topsecret"
        )
    }
}

class DbVerticle : CoroutineVerticle() {
    private val db = Database(dataSourceName = "myApp")

    override suspend fun start() {
        // ...
    }

    private suspend fun fetch(id: Int): JsonObject? {
        val res: Record? = db {
            selectFrom(DSL.table("user"))
                .where(DSL.field("id").eq(id))
                .fetchOne()
        }
        return res?.let{ convert(it) }
    }

    private suspend fun store(name: String, bio: String) {
        db.withTransaction {
            val user: Record = insertInto(DSL.table("user"))
                .set(DSL.field("name"), name)
                .returning()
                .fetchOne()!!

            insertInto(DSL.table("profile"))
                .set(DSL.field("bio"), bio)
                .set(DSL.field("user_id"), user.get(DSL.field("id")))
        }
    }


    private fun convert(record: Record): JsonObject? {
        TODO()
    }
}