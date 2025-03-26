# Module vertigram

## From Client to Building Bots

<a href="../vertigram-telegram-client/index.html">`vertigram-telegram-client`</a> left you with a full-fledged Telegram
client, allowing you to call any methods. We mentioned sending messages and stuff like that in the guide, but avoided
discussing one more important thing without which the bot cannot exist: receiving updates from Telegram. While it 
is still done with calling methods (`getUpdates` and `setWebhook`), that's pretty much everything you had for now:
implementing poller for `getUpdates` or web server for webhook was on you. While it's not a rocket science to
implement that with `vertx-web` and/or Kotlin coroutines, it's a thing needed for all the bots, so `vertigram` library
provides an implementation of that for you, along with sharing [Telegram](ski.gagar.vertigram.telegram.client.Telegram)
client across your app (remember we mentioned it being a heavy object?). As a starter, let's implement a simple bot,
answerring a `/hello` command.


```kotlin
// (1) Define logic in a verticle
class HelloVerticle : VertigramVerticle<HelloVerticle.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()
    // (2) Lazily create a telegram client
    private val tg by lazy {
        ThinTelegram(vertigram)
    }

    // (3) Define command handler
    private suspend fun handle(msg: Message) {
        if (msg.isCommandForBot("hello", typedConfig.me)) {
            tg.sendMessage(
                chatId = msg.chat.id.toChatId(),
                richText = "Hello, ${msg.from?.firstName ?: "Stranger"}".toRichText(),
                replyParameters = ReplyParameters.create(
                    messageId = msg.messageId
                )
            )
        }
    }
    
    // (4) Attach the handler
    override suspend fun start() {
        consumer<Message, Unit>(TelegramAddress.dispatchAddress(Update.Type.MESSAGE)) { handle(it) }
    }

    data class Config(
        val me: User.Me
    )
}


fun main() {
    Vertx.vertx().runBlocking {
        attachVertigram().apply {
            // (5) Deploying Telegram verticle ensemble
            deployTelegramEnsemble(
                token = "xxx:yyy",
                allowedUpdates = listOf(Update.Type.MESSAGE)
            )
            // (6) Fetching our bot info
            val tg = ThinTelegram(vertigram = this@apply)
            val me = tg.getMe()
            // (7) Deploying the command implementation verticle
            deployVerticle(HelloVerticle(), HelloVerticle.Config(me))
        }
    }
}
```

Let's go through this code, step by step:
1. The logic for our command is defined inside a
[VertigramVerticle](ski.gagar.vertigram.verticles.common.VertigramVerticle) subclass. 
If you've read <a href="../vertigram-core/index.html">`vertigram-core`</a> docs, you already see that the config for 
this verticle is a nested `Config` class and `configTypeReference` is overloaded using `typeReference` shortcut.
2. Create Telegram client to interact with Telegram. Note `lazy` initiaslization, it is needed because `vertigram` field
is not available during construction. Note that `ThinTelegram` implementation is used, all that matters for now is that
it implements `Telegram` interface, and you can call all Telegram methods using it. We'll discuss `ThinTelegram` details
later.
3. Implement the logic as a function receiving `Message` object (representing a `Message` telegram type), 
note `isCommandForBot` shortcut, it handles bot commands
in conventional formats, i.e. `/hello` and `/hello@YourBot`. That's actually the reason we're passing `me` in the config.
If we receive a command, we're sending a message in response.
4. In `start` function we're attaching our command implementation as a message handler for `Message`. Note the address,
obtained from `TelegramAddress.dispatchAddress` (we'll return to the addresses later).
5. Now, after creating `Vertx` and `Vertigram` instance, we're calling `deployTelegramEnsemble`, which deploys a set
of verticles (ensemble) which will make our bot work. That's it, now your polling the updates and publishing them so you can 
consume them from your verticles and call Telegram methods in response. This set includes:
    1. [`TelegramVerticle`](ski.gagar.vertigram.verticles.telegram.TelegramVerticle) — a verticle wrapping 
    `DirectTelegram`. It allows you to send an event bus message to call
    a Telegram method. You may argue that sending event bus messages for calling methods is less convenient than just
    calling Kotlin methods and you'll be right. That's where [`ThinTelegram`](ski.gagar.vertigram.telegram.client.ThinTelegram)
    comes into play. It wraps message sending and implements a familiar interface with plain Kotlin functions to
    call methods (as you can see in `HelloVerticle`). Unlike [`DirectTelegram`](ski.gagar.vertigram.telegram.client.DirectTelegram),\
    `ThinTelegram` is lightweight, you can create as many instances as you want, they all can talk to a single `TelegramVerticle`.
    2. [`LongPoller`](ski.gagar.vertigram.verticles.telegram.LongPoller) — a worker that continuously does long-polling 
    to receive updates and publishes them to update dispatcher (see below) or outside (optionally) the ensemble.
    3. [`UpdateDispatcher`](ski.gagar.vertigram.verticles.telegram.UpdateDispatcher) — receives updates from `LongPoller`.
    Remember `allowedUpdates` parameter for the ensemble? It is passed to `getUpdates` by `LongPoller`
    and publishes them so your workers can consume the updates (like `HelloVerticle` does). The updates are published to
    `TelegramAddress.dispatchAddress(<updateType>)`, depending on their type.
6. We create another `ThinTelegram` (now you're convinced that it's _thin_) just to fetch info about our bot using
`getMe` method.
7. Finally we're deploying our worker, passing the required configuration. Our bot is alive now and will respond to `/hello` 
command. Hooray!

Note that `HelloVerticle` was implemented in more boilerplate way than it could be to show you the basics of interaction
with Telegram verticles. In fact for handling commands there is a shortcut, `AbstractSimpleCommandVerticle`, so our
`HelloVerticle` can look like this:
```kotlin
class HelloVerticle : AbstractSimpleCommandVerticle<HelloVerticle.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()

    private val tg by lazy {
        ThinTelegram(vertigram)
    }

    override val command: String = "hello"

    override suspend fun respond(message: Message) {
        tg.sendMessage(
            chatId = message.chat.id.toChatId(),
            richText = "Hello, ${message.from?.firstName ?: "Stranger"}".toRichText(),
            replyParameters = ReplyParameters.create(
                messageId = message.messageId
            )
        )
    }

    data class Config(
        override val me: User.Me
    ) : AbstractSimpleCommandVerticle.Config {
        override val baseAddress: String = TelegramAddress.DEMUX_BASE
    }
}
```
Ignore `baseAddress` config field for now, we're going to cover it in next section.

### Customizing Vertigram ensemble

You may already wonder: "what if I want to use web-hooks instead of long polling?". We can do that!
```kotlin
deployTelegramEnsemble(
    token = "xxx:yyy",
    allowedUpdates = listOf(Update.Type.MESSAGE),
    updateReceiverConfig = WebHookConfig(
        host = "localhost",
        port = 8080
    )
)
```
Now we have a web-server listening for updates on port 8080. It's not enough for your webhook to work, you need TLS-enabled
reverse proxy (Vertigram currently does not support TLS termination). If you're up for testing it from your browser,
please note that for security reasons it expects `X-Telegram-Bot-Api-Secret-Token` to be set to your bot token 
(Telegram sets this header). The rest of the ensemble works as before.

Besides the way of receiving updates you can customize the following things (by passing them as parameters:
 - `telegramAddress` — base address used by `TelegramVerticle` to receive telegram calls. If you're unhappy with the
    default address or you want to deploy multiple telegram ensembles, you should set this property. To be able to interact 
    with customized `TelegramVerticle` you need to customize this address when creating `ThinTelegram`:
    ```kotlin
    deployTelegramEnsemble(
        token = "xxx:yyy",
        allowedUpdates = listOf(Update.Type.MESSAGE),
        telegramAddress = "com.example.myapp.telegram"
    )
    val tg = ThinTelegram(vertigram = vertigram, baseAddress = "com.example.myapp.telegram")
    ```
 - `telegramOptions`, options passed to the constructor of `DirectTelegram`
 - `throttling` — throttling options (see [ThrottlingTelegram](ski.gagar.vertigram.telegram.throttling.ThrottlingTelegram))
for more details
 - `updatePublishingAddress` — address used to communicate between `LongPoller`/`WebHook` with `UpdateDispatcher`. By default
these verticles communicate privately using mangled addresses, however if you want to listen to bare `Update`s, you can
set up this address.
 - `skipMissed` — do not try to catch up on the updates which appeared while the bot was down. By default, these updates
will be skipped, since processing them at start usually makes a mess. You can set it to `false` to change this behavior.
 - `updateDispatchAddressBase` — base address used to dispatch demultiplexed updates from `UpdateDispatcher`. If you need
to customize it, you should add a corresponding change to `TelegramAddress.dispatchAddress()` when setting up handlers:
    ```kotlin
    consumer<Message, Unit>(TelegramAddress.dispatchAddress(
        Update.Type.MESSAGE, 
        base = "com.example.myapp.telegram.updates")
    ) { 
        handle(it) 
    }
    ```
When you use `AbstractSimpleCommandVerticle` or other common verticles, `baseAddress` is this address.

## Keeping dialog state

Bot usually can maintain a chat state for a chat with a specific user and maintain multiple dialogs simultaneously.
Vertigram introduces some idiomatic ways for building such a dialogs.

Dialog is defined by a "dialog key" (it can be chat id or chat id + user id). Human user can interact with a dialog by
sending messages or by sending callback queries (clicking keyboard buttons). **Dialog is always initiated by sending 
a message**. 

Vertigram gives you building blocks to build a verticle which maintains single dialog state and dispatching updates 
between multiple simultaneous dialogs.

Let's start with the following example. This is a bot that helps user to count from 1 to 10 by sending commands and
clicking inline keyboard buttons.
```kotlin
// (1) Declaring the dialog logic
class CounterVerticle : SimpleTelegramDialogVerticle<CounterVerticle.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()
    private var count = 0
    private var lastMessageId: Long? = null

    override suspend fun start() {
        super.start()
        // (2) Implementing start
        val msg = tg.sendMessage(
            chatId = typedConfig.chatId.toChatId(),
            richText = "Let's count together! Type $CMD or click the button".toRichText(),
            replyMarkup = inlineKeyboard {
                row {
                    callback("+1", PLUS_CALLBACK)
                }
            }
        )
        // (3) Remembering last message so we can edit it
        lastMessageId = msg.messageId
    }

    // (4) Ste state changing logic
    private fun inc() = ++count

    // (5) Replying as a message to report curent count and possibly exit
    private suspend fun reply() {
        if (lastMessageId != null) {
            // (6) Removing buttons from old message
            tg.editMessageReplyMarkup(
                chatId = typedConfig.chatId.toChatId(),
                messageId = lastMessageId!!,
            )
        }
        // (7) If we're done, telling user so and completing our work, CounterVerticle's live is over here (die() call)
        if (count == MAX_COUNT) {
            tg.sendMessage(
                chatId = typedConfig.chatId.toChatId(),
                richText = "We've counted towards $count and done. Type $CMD if you want to start over!".toRichText()
            )
            die(DeathReason.COMPLETED)
            return
        }
        // (8) Proceeding? Let's report current status to the user
        val msg = tg.sendMessage(
            chatId = typedConfig.chatId.toChatId(),
            richText = "Current count is $count. Type $CMD or click the button to proceed.".toRichText(),
            replyMarkup = inlineKeyboard {
                row {
                    callback("+1", PLUS_CALLBACK)
                }
            }
        )
        lastMessageId = msg.messageId
    }

    // (9) Handler for a message. Only messages from current dialog can arrive here. You can avoid boilerplate 
    //     checking for chatId
    override suspend fun handleMessage(message: Message) {
        if (!message.isCommandForBot(CMD, typedConfig.me)) {
            return
        }
        inc()
        reply()
    }

    // (9) Handler for a callback query. Only messages from current dialog can arrive here. You can avoid boilerplate 
    //     checking for chatId
    override suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) {
        if (callbackQuery.data != PLUS_CALLBACK) {
            return
        }
        inc()
        reply()
    }

    data class Config(
        val chatId: Long,
        val me: User.Me,
        val timeout: Duration // (10) We'll come to timeout implementation later, for now, ignore it
    )

    companion object {
        const val MAX_COUNT = 10
        const val PLUS_CALLBACK = "+"
        const val CMD = "count"
    }
}

// (11) Dispatched for managing multiple dialogs simultaneously
class CounterDispatchVerticle : AbstractDispatchVerticle.ByChatAndUser<CounterDispatchVerticle.Config, CounterVerticle.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()

    // (12) By default initial message is not passed to dialog verticle, you can do it manually
    // override val passInitialMessageToChild: Boolean = true

    // (13) Init logic for a new dialog
    override fun initChild(dialogKey: DialogKey, msg: Message): Deployment<CounterVerticle.Config>? {
        if (!msg.isCommandForBot(CounterVerticle.CMD, typedConfig.me))
            return null

        return Deployment(
            CounterVerticle(),
            CounterVerticle.Config(
                chatId = msg.chat.id,
                me = typedConfig.me,
                timeout = Duration.ofMinutes(1)
            )
        )
    }

    data class Config(
        val me: User.Me,
        override val baseAddress: String = TelegramAddress.DEMUX_BASE,
        override val verticleAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE
    ) : AbstractDispatchVerticle.Config
}

fun main() {
    Vertx.vertx().runBlocking {
        attachVertigram().apply {
            deployTelegramEnsemble(
                token = "xxx:yyy",
                allowedUpdates = listOf(Update.Type.MESSAGE, Update.Type.CALLBACK_QUERY)
            )
            val tg = ThinTelegram(vertigram = this@apply)
            val me = tg.getMe()
            // (14) Deploying dispatcher. Note that we do not explicitly deploying CounterVerticle here
            deployVerticle(CounterDispatchVerticle(), CounterDispatchVerticle.Config(me))
        }
    }
}
```

Let's walk through it step-by-step:
1. `CounterVerticle` is a subclass of `SimpleTelegramDialogVerticle`. If used correctly in conjunction with dispatch 
verticle, it willr eceive only updates for a specific dialog. That means you don't have to bother with checking chat ids.
2. When the verticle is started, it's a good thing to greet user and give him instructions by sending a message in 
response.
3. We save `lastMessageId` because later we're going to edit messages which are already sent. This is a part of
a **dialog state**
4. Other *useful* part of the **state** is a counter itself.
5. Let's define a *response function* which will send the reaction to the current state update
6. First, we remove the buttons from old message, so there are not too many buttons. (Remember the step 3?)
7. If we're reached 10, our job is done, we can terminate `CounterVerticle` by calling `die()` (for more info on the lifecycle,
please consult [AbstractHierarchyVerticle](ski.gagar.vertigram.verticles.common.AbstractHierarchyVerticle). Don't forget
to tell the user that we're done
8. Otherwise, updating the user with new counter value
9. Now we override `handleMessage` function to implement bot's reaction to the message from user:
checking if we've got a command from the user and if so, incrementing the counter and replying user with function from 
steps 5-8
10. An extra config parameter which we'll use later
11. Now let's implement **dispatch verticle** which will manage state for multiple dialogs. We extend
`AbstractDispatchVerticle.ByChatAndUser` to make it dispatch updates by chat id + user id. Generic parameters are 
config for dispatch verticle itself and config for child verticle.
12. Initial message is not passed by default to the freshly deployed verticle. This happens because
there is no right way to separate initialization logic from here to `SimpleTelegramDialogVerticle`. You may have done
some complex work while deciding to start the verticle (e.g. parsing the command) which you don't want to redo.
However, in our case it's safe to pass the initial message to the `CounterVerticle`. Try it out while playing around
with the code. The behavior will be slightly different: the right behavior depends on what you want.
13. We're describing how to create a new **dialog verticle**. `initChild` is called
only if the dialog is not yet started for current **dialog key**. `AbstractDispatchVerticle` logic expects you to return
`Deployment` object if you decide to deploy something based on message content or `null` if the message should be ignored.
14. Finally, deploying the `CounterDispatchVerticle`. Do not forget to enable receiving `CALLBACK_QUERY` updates.

Start the bot and try to play around with it. If you have multiple accounts, you can notice that every one of them
will have separate counter.

### Dialog keys

In previous example we boldly used `AbstractDispatchVerticle.ByChatAndUser` as a base class to dispatch dialogs by
chat id + user id pair. 
Other viable option is `AbstractDispatchVerticle.ByChat`. 

You can notice the difference in group chats: in first case, if you start the counting party in the group chat, 
each member will have a separate counter, while with chat id dialog key, all chat members will count together!

If both of these options do not work for you, you can implement `AbstractDispatchVerticle.DialogKey` yourself
and use `AbstractDispatchVerticle` directly and override the corresponding methods to extract dialog keys from
messages and callback queries.

### Concurrency and locks

You might've noticed that we didn't implement any measures to implement state consistency. First reason for that is that
the dialogs are isolated from each other. Vert.x verticles also provides you some level of thread safety, when
you implement a verticle, you don't have to worry about races. However, it's not always that simple. If a new message
arrives while the previous handler is **suspended**, verticle can start handling the new message in between of handling 
the old one. Our example is still safe though, we're updating the counter *atomically* inside non-suspendable section of
the code (`inc` function). However it's not always possible to do it that way. Let's pretend our `inc` function does not
simply update the class field, but instead contacts some imaginary cloud abacus service, which does not have any support
for atomic increments:
```kotlin
private suspend fun inc() {
    val count = this.count
    delay(10000)
    this.count = count + 1
}
```

Let's start our bot and try starting counting and incrementing the counter two times in a row. In ten seconds, you'll
get a bit weird results. Our counter turns out not to be so good! And all because of suspending done by `delay` call.
Fortunately, Vertigram provides you a quick way to fix it! Let's wrap our handlers with `withLock` call:
```kotlin
override suspend fun handleMessage(message: Message) {
    withLock {
        if (!message.isCommandForBot(CMD, typedConfig.me)) {
            return
        }
        inc()
        reply()
    }
}

override suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) {
    withLock {
        if (callbackQuery.data != PLUS_CALLBACK) {
            return
        }
        inc()
        reply()
    }
}
```

Let's restart and try again. Much better, huh? The second `/count` was ignored, because we were busy with the first one.
While it makes behavior more reasonable, it's not always what you want. There is also an option to enqueue second `/count`
call instead of just ignoring it, by callint `withLock` this way:
```kotlin
withLock(discardWhenBusy = false) {
    // ...
}
```

Try changing your code and sheck that both of your slow `/count`s have been handled. After 20 seconds you'll get a 
correct value for your counter.

You might want to give user some feedback if the dialog worker is busy. In that case you're free to implement your own 
locking logic, totally ignoring `withLock`. Fortunately, with coroutines it's easily implementable using naive way
with boolean flags:
```kotlin
private var busy = false
private suspend inline fun oneAtATime(block: () -> Unit) {
    if (busy) {
        tg.sendMessage(
            chatId = typedConfig.chatId.toChatId(),
            richText = "I am busy with your previous command, hold on.".toRichText()
        )
        return
    }
    busy = true
    try {
        block()
    } finally {
        busy = false
    }
}
```

You may wonder: now we have a `busy` as a state which you need to handle in a proper way. If you track the code,
you'll notice that check and update (in case worker is not busy is done without suspension, i.e. *atomically*), so
we're good. Now just wrap handlers in your new locking function:
```kotlin
override suspend fun handleMessage(message: Message) {
    oneAtATime {
        if (!message.isCommandForBot(CMD, typedConfig.me)) {
            return@handleMessage
        }
        inc()
        reply()
    }
}

override suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) {
    oneAtATime {
        if (callbackQuery.data != PLUS_CALLBACK) {
            return@handleCallbackQuery
        }
        inc()
        reply()
    }
}
```

### Bonus: Adding Timeout

What happens if user starts counting and then leaves forever at the count of 6? His dialog will be stored in memory
until the bot is restarted. This is not a very comforting thought, right. Let's add some handling for it:
```kotlin
var timer: Job? = null
fun resetTimer() {
    if (null != timer) {
        timer?.cancel()
    }
    timer = setTimerNonCancellable(typedConfig.timeout) {
        tg.sendMessage(
            chatId = typedConfig.chatId.toChatId(),
            richText = "Ok, bye!".toRichText()
        )
        die(DeathReason.TIMEOUT)
    }
}
```

`resetTimer` will reset timeout timer if it is set and set a new one 60 seconds from now. Let's call it in the places
we want to reset it: in `start`, `handleMessage` and `handleCallbackQuery` functions. Try it out, now *dialog verticle*
will say "Bye" to you after a minute of inactivity. calling `die` with a proper reason notifies **dispatch verticle**
to forget about this child and properly free up resources associated with it.

## Advanced State Management

`SimpleTelegramDialogVerticle` in conjunction with `AbstractDispatchVerticle` give you a simple way to manage 
individual dialog states. In real bots, dialog states may be more complicated (imagine typical step-by-step 
questionnaire). While it is still possible to manage it manually with `SimpleTelegramDialogVerticle`, Vertigram provides
an opinionated way to maintain more complex dialog state, named `StatefulTelegramDialogVerticle`. Besides more advanced
state management it provides some out-of-the-box ways to manage timeouts, cancellation and completion. Let's try
to implement a bot that collects user age, name and his favourite color: