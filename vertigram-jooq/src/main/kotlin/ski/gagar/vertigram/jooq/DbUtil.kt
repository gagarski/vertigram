package ski.gagar.vertigram.jooq

import com.zaxxer.hikari.HikariDataSource
import io.vertx.core.Vertx
import io.vertx.core.shareddata.Shareable
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
    throw IllegalArgumentException("Shared data source $name is not present")

internal fun Vertx.deleteSharedDataSource(name: String) {
    sharedData().getLocalMap<String, ShareableHolder<DataSourceWithUrl>>(DATA_SOURCES_MAP_NAME)
        .compute(name) { _, old ->
            if (old != null)
                throw IllegalStateException("The data source with this name is already present")

            null
        }
}

