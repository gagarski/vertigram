# Module vertigram-jooq-gradle-plugin

Plugin that runs opinionated migration and code-generation process for `vertigram-jooq`.

It optionally involves running `testcontainers` database to perform migration and code generation 
without live database running on your machine.

It somehow combines the functionality of the following plugins:
 - `org.jooq.jooq-codegen-gradle`
 - `org.flywaydb.flyway`

The migration and code generation process can be run either against a live database, running on your machine 
or against a database deployed internally in a Docker container. For the latter, running Docker daemon is required.

## Examples
Basic example with embedded Postgres:
```kotlin
plugins {
    id("ski.gagar.vertigram.jooq")
}

vertigramJooq {
    postgresTestContainer("16.1")
    jooq {
        packageName = "com.example.db"
    }
}
```

Verbose syntax for fine-tuning:
```kotlin
plugins {
    id("ski.gagar.vertigram.jooq")
}

vertigramJooq {
    testContainer {
        className = "org.testcontainers.containers.PostgreSQLContainer"
        name = "postgres"
        version = "16.1"
    }
    
    flyway {
        locations = listOf("filesystem:src/main/resources/db/migration")
    }
    
    jooq {
        driver = "org.postgresql.Driver"
        dbName = "org.jooq.meta.postgres.PostgresDatabase"
        includes = ".*"
        inputSchema = "public"
        packageName = "com.example.db"
    }
}
```

## Using with a Live DB
If you really don't want to run docker on your machine but rather run database instance, you still can use it for code generation:
```kotlin
plugins {
    id("ski.gagar.vertigram.jooq")
}

vertigramJooq {
    liveDb {
        url = "jdbc:postgresql://localhost/myapp"
        username = "myapp"
        password = "password" // Better to externalize
    }

    jooq {
        packageName = "com.example.db"
    }
}
```