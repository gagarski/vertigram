package ski.gagar.vertigram.samples

import io.vertx.kotlin.coroutines.CoroutineVerticle
import ski.gagar.vertigram.jooq.Database

private suspend fun CoroutineVerticle.dbExample() {
    val db = Database()
    db {
        selectFrom("some_table")
            .fetch()
    }
}
private suspend fun CoroutineVerticle.dbTranExample() {
    val db = Database()
    db.withTransaction {
        selectFrom("some_table")
            .fetch()
    }
}
