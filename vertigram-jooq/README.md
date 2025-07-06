# Module vertigram-jooq

# Features

`vertigram-jooq` is a bit standalone project aside from the rest of `vertigram`. It has no dependency of
`vertigram-core` and `vertigram`, which means you either can use it with plain Vert.X.

The main goal of this library is to provide a way to create locally shared JDBC datasources and provide a way
to query them from verticles.

# Usage

Creating shared datasources is pretty straightforward:
```kt
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
```

`createSharedDataSource` is basically a wrapper around standard Vert.X `sharedData()`. There is also a counterpart method
that allows you to retrieve the datasource and delete it:
 - `Vertx.getSharedDatasource`
 - `Vertx.deleteSharedDatasource`

Of course, that won't be very useful on its own. Besides that `vertigram-jooq` provides you an API to access these datasources from
a verticle and perform queries using jOOQ DSL.

```kt
class DbVerticle : CoroutineVerticle() {
    private val db = Database(dataSourceName = "myApp")

    override suspend fun start() {
        // ...
    }

    private suspend fun fetch(id: Int): JsonObject? {
        val res: Record? = db {
            selectFrom("user")
                .where(DSL.field("id").eq(id))
                .fetchOne()
        }
        return res?.let { convert(it) }
    }
    
    private fun convert(record: Record): JsonObject? {
        TODO()
    }
}
```

First, you need to create `Database` object inside your verticle. Then you can `invoke` this object passing lambda,
containing jOOQ query DSL.

Using transactions is also pretty straightforward:
```kt
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
```

# What's Next

The examples above show pretty basic usage of jOOQ. Using jOOQ is much more fun in conjunction with code generation.
You can either set up code generation in your build process the way you're familiar with or get familiar with
<a href="../vertigram-jooq-gradle-plugin/index.html">`vertigram-jooq-gradle-plugin`</a>, which executes jOOQ code generation
along with Flyway migrations, optionally using power of Testcontainers to create a temporary database used for code generation.