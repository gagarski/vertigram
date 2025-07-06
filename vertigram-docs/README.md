# Module Vertigram

Vertigram is a framework to build Telegram bots using Vert.X and Kotlin with coroutines.

## Getting started

Besides providing a basic wrapper around Telegram API Vertigram introduces JSON-based protocol for
event-bus communication and some convenience methods and tools to build your bot.

Vertigram consists of several modules, each having its own documentation page. You can start reading in the following order:
 - <a href="./vertigram-telegram-client/index.html">`vertigram-telegram-client`</a> provides you only with
    Telegram API Kotlin coroutine wrapper. If you don't want anything besides that, you can stop at this point and 
    use it with your plain Vert.X app
 - <a href="./vertigram-core/index.html">`vertigram-core`</a> introduces Vertigram-specific verticles and event bus
    protocols, which are widely used in bot building tools provided by Vertigram
 - <a href="./vertigram/index.html">`vertigram`</a> gives you bot building tools, such as verticles for dispatching 
    messages to dialogs and managing dialog state. It also introduces a ways to properly share a `Telegram` client
    across your app and receive updates from Telegram server.

Few extra modules stand aside:
 - <a href="./vertigram-jooq/index.html">`vertigram-jooq`</a> provides you an API to share JDBC datasources across your
    app and query them using jOOQ API
 -  <a href="./vertigram-jooq-gradle-plugin/index.html">`vertigram-jooq-gradle-plugin`</a> is a Gradle plugin to execute 
    jOOQ code generation along with Flyway migrations and optionally using Testcontainers to run temporary database,
    used for code generation.
 - <a href="./vertigram-logback/index.html">`vertigram-logback`</a> provides you with some stuff allowing to
    interoperate with logback for publishing log events over event bus or to Telegram.

## Telegram API versions

| Vertigram Version | Telegram API Version |
|-------------------|----------------------|
| 1.0               | 9.1                  |