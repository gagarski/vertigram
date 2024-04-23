# Module vertigram-core

`vertigram-core` provides wrappers around core Vert.x widely used by <a href="../vertigram/index.html">`vertigram`</a>
module for communication on top of Vert.x event bus and gives you a way to use the same wrappers in your code.

Core features:
 - Using JSON-serialization with Jackson for event bus messages
 - Throwing exceptions over event bus
 - Coroutine-first API on top of event bus.

## `Vertigram` object.

When you're starting your project with Vert.x, first thing you're likely to do is creating [Vertx](io.vertx.core.Vertx) 
instance, with Vertigram, you're likely to create [Vertigram](ski.gagar.vertigram.Vertigram) instance on top of 
[Vertx](io.vertx.core.Vertx), by calling [attachVertigram](ski.gagar.vertigram.attachVertigram) extension function on 
[Vertx](io.vertx.core.Vertx) instance.

[Vertigram](ski.gagar.vertigram.Vertigram) is an object that provides you the following:
 - access to [Vertigram.EventBus](ski.gagar.vertigram.Vertigram.EventBus)
 - namespaces on top of Vertx [EventBus](io.vertx.core.eventbus.EventBus)
 - methods to deploy a [VertigramVerticle](ski.gagar.vertigram.verticles.common.VertigramVerticle)

[attachVertigram](ski.gagar.vertigram.attachVertigram) takes a `config` as an argument, you can configure the following:
 - name of attached [Vertigram](ski.gagar.vertigram.Vertigram) which will be used for namespacing (meaning that 
   you can attach multiple [Vertigram](ski.gagar.vertigram.Vertigram)s to a single [Vertx](io.vertx.core.Vertx))
 - custom [ObjectMapper](com.fasterxml.jackson.databind.ObjectMapper) to customize a way messages are being serialized
 - options, fine-tuning the exception handling or other aspects of [Vertigram.EventBus](ski.gagar.vertigram.Vertigram.EventBus)
   protocol
 - `initializers` that will be executed after creating [Vertigram](ski.gagar.vertigram.Vertigram) instance (if not specified,
   then initalizers will be discovered using [ServiceLoader](java.util.ServiceLoader))

## Vertigram Event Bus

[Vertigram.EventBus](ski.gagar.vertigram.Vertigram.EventBus) is a wrapper around Vertx event bus which provides you 
with some familiar event bus operations on top of Vertx event bus operations:
 - [Vertigram.EventBus.publish](ski.gagar.vertigram.Vertigram.EventBus.publish)
 - [Vertigram.EventBus.send](ski.gagar.vertigram.Vertigram.EventBus.send)
 - [Vertigram.EventBus.request](ski.gagar.vertigram.Vertigram.EventBus.request)
 - [Vertigram.EventBus.consumer](ski.gagar.vertigram.Vertigram.EventBus.consumer)

The semantic of these operations is pretty much the same as for the corresponding operations on Vertx event bus. 
The key features are:
 - coroutine-first API, unlike standard ones which provide you either a callback-based API or deal with futures
 - typing and serialization/deserialization using Jackson: all things that can be typed are typed
 - consumers can throw exceptions which are passed to [Vertigram.EventBus.request](ski.gagar.vertigram.Vertigram.EventBus.request)
   caller with flexible level of verbosity
 - namespaced addresses, meaning that messages sent inside one [Vertigram](ski.gagar.vertigram.Vertigram) are not visible
   by other [Vertigram](ski.gagar.vertigram.Vertigram)s.


## Vertigram Verticles

Like in core Vert.x, the basic worker of your app will be a Verticle. Basic verticle in `vertigram-core` is 
[VertigramVerticle](ski.gagar.vertigram.verticles.common.VertigramVerticle). Key features are:
 - [VertigramVerticle](ski.gagar.vertigram.verticles.common.VertigramVerticle) is a 
   [CoroutineVerticle](io.vertx.kotlin.coroutines.CoroutineVerticle), which gives you all the support for courutines in
   verticles that Vert.x give you
 - Access to associated [Vertigram](ski.gagar.vertigram.Vertigram) instance and therefore to 
   [Vertigram.EventBus](ski.gagar.vertigram.Vertigram.EventBus) and its operations
 - Type-safe configuration in conjunction with [deployVerticle](ski.gagar.vertigram.Vertigram.deployVerticle) function
 - Overridable name for the verticle which can be used for logging

## Clustering and interoperation

As you may have noticed, Vertigram uses the same basic concepts as Vert.x, providing them with some convenient 
improvements. If fact, [VertigramVerticle](ski.gagar.vertigram.verticles.common.VertigramVerticle) is a usual Vert.x 
verticle and [Vertigram.EventBus](ski.gagar.vertigram.Vertigram.EventBus) operates on top of regular Vert.x event bus.

[Vertigram](ski.gagar.vertigram.Vertigram) is a **purely local** object (in fact, stored in Vert.x shared local map), 
that basically contains its name and [ObjectMapper](com.fasterxml.jackson.databind.ObjectMapper) instance. If you
want to use Vertigram in clustered environments, you should have a compatible instance of 
[Vertigram](ski.gagar.vertigram.Vertigram) attached to each [Vertx](io.vertx.core.Vertx) node. Compatible means
that they have the same name and their mappers can deserialize messages produced by each other. 

[Vertigram.EventBus](ski.gagar.vertigram.Vertigram.EventBus) based on standard event bus allows you to talk to it
from standard event bus, but for that you'd like to know some protocol details which are shared in 
[Vertigram.EventBus](ski.gagar.vertigram.Vertigram.EventBus) KDoc. This may come in handy if you want to communicate in
your app from non-Kotlin environment (which still knows how to talk to event bus). In that case you may need to reimplement
parts of this protocol in your code.

## Event bus exceptions handling

As mentioned above, consumers on Vertigram event bus can throw exceptions. There are some conventions that exist for these
exceptions:
 - Exceptions inherited from [VertigramException](ski.gagar.vertigram.util.exceptions.VertigramException) thromn in consumer
   will be rethrown in the code that calls [Vertigram.EventBus.request](ski.gagar.vertigram.Vertigram.EventBus.request)
 - ... except for exceptions which are sub-classes of 
   [VertigramInternalException](ski.gagar.vertigram.util.exceptions.VertigramInternalException), which are being rethrown
   by `request`inf code as bare [VertigramInternalException](ski.gagar.vertigram.util.exceptions.VertigramInternalException),
   optionally keeping the original message (based on Vertigram configuration)
 - Other exceptions behave as [VertigramInternalException](ski.gagar.vertigram.util.exceptions.VertigramInternalException)

## Showcase

Let's build a small example, demonstrating simple event bus based interaction with the verticle:
```kotlin
import com.fasterxml.jackson.core.type.TypeReference
import io.vertx.core.Vertx
import ski.gagar.vertigram.util.jackson.typeReference
import ski.gagar.vertigram.verticles.common.VertigramVerticle

// (1) Config is a type parameter
class GreeterVerticle : VertigramVerticle<GreeterVerticle.Config>() {
    // (2) A small hack to make type reified
    override val configTypeReference: TypeReference<Config> = typeReference()

    // (3) Define consumer logic, note the absence of serialization/deserialization and `await`s here
    private fun handleMessage(req: Request) =
        Response("${typedConfig.greeting} ${req.name}")

    override suspend fun start() {
        // (4) Attach the consumer
        consumer<Request, Response>("greet") {
            handleMessage(it)
        }
    }

    data class Request(val name: String)
    data class Response(val greeting: String)
    data class Config(val greeting: String = "Hello")
}


fun main() {
    Vertx.vertx().runBlocking {
        attachVertigram().apply {
            // (5) Deploy the verticle, deployVErticle won't let you mistype the config, also notice absence of the `await` call
            deployVerticle(GreeterVerticle(), GreeterVerticle.Config("Bonjour"))

            // (6) Interact with the verticle, some type hints are needed for the response type
            val resp: GreeterVerticle.Response = eventBus.request("greet", GreeterVerticle.Request("Bill"))
            println(resp)
        }
    }
}
```

## What's next

So far you know `vertigram-core` basics. Besides the concepts described above, provides you some generic verticles for common usage
in [ski.gagar.vertigram.verticles.common] package. Currently the following verticles are provided:
 - [AbstractHierarchyVerticle](ski.gagar.vertigram.verticles.common.AbstractHierarchyVerticle), providing you with
   a concept of hierarchy in verticles (i.e. spawning child verticles and notifying parents and child about termination of each other)
 - [AbstractPostOfficeVerticle](ski.gagar.vertigram.verticles.common.AbstractPostOfficeVerticle) which provides you 
   a concept of mailboxes for replaying messages to a consumer which have subscribed to them later than messages were
   published

If all you were looking for was some kind of Vert.x booster, then you're good to go. If you're still up for building some
Telegram bots and already familiar with <a href="../vertigram-telegram-client/index.html">`vertigram-telegram-client`</a>
module, you can jump to <a href="../vertigram/index.html">`vertigram`</a> module, which combines power of 
<a href="../vertigram-telegram-client/index.html">`vertigram-telegram-client`</a> and
<a href="../vertigram-core/index.html">`vertigram-core`</a>.
