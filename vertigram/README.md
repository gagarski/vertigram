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
    // (2) Lazily create a telegram client
    private val tg by lazy {
        ThinTelegram(vertigram)
    }

    // (3) Define command handler
    private suspend fun handle(msg: Message) {
        if (msg.isCommandForBot("hello", typedConfig.me)) {
            tg.sendMessage(
                chatId = msg.chat.id.toChatId(),
                text = "Hello, ${msg.from?.firstName ?: "Stranger"}".toFormattedText(),
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
this verticle is a nested `Config` class. Its runtime type is inferred automatically from the superclass declaration.
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
with Telegram verticles. In fact for handling commands there is a shortcut, `SimpleCommandVerticle`, so our
`HelloVerticle` can look like this:
```kotlin
class HelloVerticle : SimpleCommandVerticle<HelloVerticle.Config>() {
    private val tg by lazy {
        ThinTelegram(vertigram)
    }

    override val command: String = "hello"

    override suspend fun respond(message: Message) {
        tg.sendMessage(
            chatId = message.chat.id.toChatId(),
            text = "Hello, ${message.from?.firstName ?: "Stranger"}".toFormattedText(),
            replyParameters = ReplyParameters.create(
                messageId = message.messageId
            )
        )
    }

    data class Config(
        override val me: User.Me
    ) : SimpleCommandVerticle.Config {
        override val baseAddress: String = TelegramAddress.DEMUX_BASE
    }
}
```
Ignore `baseAddress` config field for now, we're going to cover it in next section.

### TelegramVerticle messaging protocol

[`ThinTelegram`](ski.gagar.vertigram.telegram.client.ThinTelegram) normally hides the event-bus protocol, but callers
can also communicate with [`TelegramVerticle`](ski.gagar.vertigram.verticles.telegram.TelegramVerticle) directly.
A Telegram method consumer uses this Vertigram address:

```text
<telegramAddress>.<methodAddress>.<transport>
```

- `telegramAddress` is the configured Telegram verticle base address. Its default value is
  `ski.gagar.vertigram.telegram.verticle`.
- `methodAddress` defaults to the callable's simple class name with its first letter lowercased. Nested class segments
  are lowercased independently and joined with dots. It can be overridden by
  `TelegramCodegen.Method.verticleConsumerName`.
- `transport` is `json` or `multipart`, according to the callable's HTTP transport.

For example, `AddStickerToSet` uses:

```text
ski.gagar.vertigram.telegram.verticle.addStickerToSet.multipart
```

The nested callable `EditMessageCaption.InlineMessage` uses:

```text
ski.gagar.vertigram.telegram.verticle.editMessageCaption.inlineMessage.json
```

The request payload is the corresponding `TelegramCallable`, and the response payload is that callable's return type.
Use `TelegramVerticle.Config.callAddress(...)` to obtain an address programmatically instead of reconstructing it.
`getUpdates` is available at `<telegramAddress>.getUpdates.json`; configuration and file-download operations use the
dedicated helpers on `TelegramVerticle.Config`.

### Customizing Vertigram ensemble

You may already wonder: "what if I want to use web-hooks instead of long polling?". We can do that!
```kotlin
deployTelegramEnsemble(
    token = "xxx:yyy",
    allowedUpdates = listOf(Update.Type.MESSAGE),
    updateReceiverConfig = WebHookConfig(
        host = "localhost",
        port = 8080,
        secretToken = "replace-with-a-stable-random-secret"
    )
)
```
Now we have a web-server listening for updates on port 8080. It's not enough for your webhook to work, you need TLS-enabled
reverse proxy (Vertigram currently does not support TLS termination). If you're up for testing it from your browser,
please note that for security reasons it expects `X-Telegram-Bot-Api-Secret-Token` to match `secretToken` from
`WebHookConfig` (Telegram sets this header). Telegram accepts 1 to 256 characters from `A-Z`, `a-z`, `0-9`, `_`, and `-`.

Explicitly provide an independently generated, stable `secretToken` in production. A random value of at least 32
characters is recommended. Store it alongside your other application secrets, and use the same value for every instance
serving the webhook. Do not use the Telegram bot token, and do not transform the bot token by replacing its `:` character:
that would turn a webhook-secret leak into a bot-token leak.

If `secretToken` is omitted, Vertigram generates a random UUID and logs a warning. That value is not exposed or persisted,
so it changes on every restart. The fallback is convenient for local development, but it causes webhook authentication
failures during restarts and is not suitable for rolling or multi-instance deployments.

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
When you use `SimpleCommandVerticle` or other common verticles, `baseAddress` is this address.

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
    private var count = 0
    private var lastMessageId: Long? = null

    override suspend fun start() {
        super.start()

    }

    // (2) Ste state changing logic
    private fun inc() = ++count

    // (3) Replying as a message to report current count and possibly exit
    private suspend fun reply() {
        if (lastMessageId != null) {
            // (4) Removing buttons from old message
            tg.editMessageReplyMarkup(
                chatId = typedConfig.chatId.toChatId(),
                messageId = lastMessageId!!,
            )
        }
        // (5) If we're done, telling user so and completing our work, CounterVerticle's live is over here (die() call)
        if (count == MAX_COUNT) {
            tg.sendMessage(
                chatId = typedConfig.chatId.toChatId(),
                text = "We've counted towards $count and done. Type $CMD if you want to start over!".toFormattedText()
            )
            die(DeathReason.COMPLETED)
            return
        }
        // (6) Proceeding? Let's report current status to the user
        val msg = tg.sendMessage(
            chatId = typedConfig.chatId.toChatId(),
            text = textMarkdown {

                if (count == 0) {
                    +"Hello! Let's count together!"
                    space()
                }
                +"Current count is $count. Type $CMD or click the button to proceed."
            },
            replyMarkup = inlineKeyboard {
                row {
                    callback("+1", PLUS_CALLBACK)
                }
            }
        )
        lastMessageId = msg.messageId
    }

    // (7) Handler for a message. Only messages from current dialog can arrive here. You can avoid boilerplate
    //     checking for chatId
    override suspend fun handleMessage(message: Message) {
        if (!message.isCommandForBot(CMD, typedConfig.me)) {
            return
        }
        inc()
        reply()
    }

    // (8) Handler for a callback query. Only messages from current dialog can arrive here. You can avoid boilerplate
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
        val timeout: Duration // (9) We'll come to timeout implementation later, for now, ignore it
    )

    companion object {
        const val MAX_COUNT = 10
        const val PLUS_CALLBACK = "+"
        const val CMD = "count"
    }
}

// (10) Dispatched for managing multiple dialogs simultaneously
class CounterDispatchVerticle : DispatchVerticle.ByChatAndUser<CounterDispatchVerticle.Config, CounterVerticle.Config>() {
    // (11) Suspendable preparation logic for a new dialog
    override suspend fun prepareChild(dialogKey: DialogKey, msg: Message): Deployment<CounterVerticle.Config>? {
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
    ) : DispatchVerticle.Config
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
            // (12) Deploying dispatcher. Note that we do not explicitly deploying CounterVerticle here
            deployVerticle(CounterDispatchVerticle(), CounterDispatchVerticle.Config(me))
        }
    }
}
```

Let's walk through it step-by-step:
1. `CounterVerticle` is a subclass of `SimpleTelegramDialogVerticle`. If used correctly in conjunction with dispatch 
verticle, it willr eceive only updates for a specific dialog. That means you don't have to bother with checking chat ids.
2. *Useful* part of the **state** is a counter itself.
3. Let's define a *response function* which will send the reaction to the current state update
4. First, we remove the buttons from old message, so there are not too many buttons. (Remember the step 3?)
5. If we're reached 10, our job is done, we can terminate `CounterVerticle` by calling `die()` (for more info on the lifecycle,
please consult [HierarchyVerticle](ski.gagar.vertigram.verticles.common.HierarchyVerticle). Don't forget
to tell the user that we're done
6. Otherwise, updating the user with new counter value
7. Now we override `handleMessage` function to implement bot's reaction to the message from user:
checking if we've got a command from the user and if so, incrementing the counter and replying user with function from 
steps 2-6.
8. Also let's override `handleCallbackQuery` to work with keyboard.
9. An extra config parameter which we'll use later
10. Now let's implement **dispatch verticle** which will manage state for multiple dialogs. We extend
`DispatchVerticle.ByChatAndUser` to make it dispatch updates by chat id + user id. Generic parameters are 
config for dispatch verticle itself and config for child verticle.
11. We're describing how to create a new **dialog verticle**. `prepareChild` is suspendable and is called
only if the dialog is not yet started for current **dialog key**. `DispatchVerticle` logic expects you to return a
`Deployment` candidate if you decide to deploy something based on message content or `null` if the message should be
ignored. If concurrent messages prepare multiple candidates, only one is deployed.
12. Finally, deploying the `CounterDispatchVerticle`. Do not forget to enable receiving `CALLBACK_QUERY` updates.

Start the bot and try to play around with it. If you have multiple accounts, you can notice that every one of them
will have separate counter.

### Dialog keys

In previous example we boldly used `DispatchVerticle.ByChatAndUser` as a base class to dispatch dialogs by
chat id + user id pair. 
Other viable option is `DispatchVerticle.ByChat`. 

You can notice the difference in group chats: in first case, if you start the counting party in the group chat, 
each member will have a separate counter, while with chat id dialog key, all chat members will count together!

If both of these options do not work for you, you can implement `DispatchVerticle.DialogKey` yourself
and use `DispatchVerticle` directly and override the corresponding methods to extract dialog keys from
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
            text = "I am busy with your previous command, hold on.".toFormattedText()
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
            text = "Ok, bye!".toFormattedText()
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

`SimpleTelegramDialogVerticle` in conjunction with `DispatchVerticle` give you a simple way to manage 
individual dialog states. In real bots, dialog states may be more complicated (imagine typical step-by-step 
questionnaire). While it is still possible to manage it manually with `SimpleTelegramDialogVerticle`, Vertigram provides
an opinionated way to maintain more complex dialog state, named `StatefulTelegramDialogVerticle`. Besides more advanced
state management it provides history, rollback, and an optional absolute dialog timeout.

Dialog states derive from `AbstractState` and choose one of two delivery models. `State` uses regular delivery and may
handle regular messages, ephemeral messages, and callbacks. `EphemeralState` handles ephemeral messages and callbacks;
its `sendOrEdit` uses the hook installed for the current interaction. Neither state constructor accepts a hook. Enter an
ephemeral state with `become(newState, ephemeralHook)` and a regular state with `become(newState)`. Both calls require the
dialog lock, which handlers, side effects, and `setTimer` already hold. For another coroutine, wrap the transition
in `withLock { become(...) }`.

An ephemeral timer receives the hook captured when it was scheduled:

```kotlin
setTimer(Duration.ofSeconds(5)) { hook ->
    sendOrEdit("Still waiting".toFormattedText())
    become(NextEphemeralState(verticle), hook)
}
```
Let's try to implement a bot that collects user age, name and his favourite color:

```kotlin
// (1) Some helper for reply markup
const val BACK = "back"

fun InlineKeyboardMarkupRowBuilder.backButton(canGoBack: Boolean) {
    if (canGoBack) {
        callback(
            text = "↩️ Back",
            callbackData = BACK
        )
    }

}

// (2) Single-dialog logic
class RegisterVerticle : StatefulTelegramDialogVerticle<RegisterVerticle.Config>() {
    // (3) Defining the states
    sealed class State(private val verticle: RegisterVerticle) : StatefulTelegramDialogVerticle.State(verticle) {
        protected val typedConfig: Config
            get() = verticle.typedConfig

        final override suspend fun shouldHandleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ): Boolean {
            if (callbackQuery.data != BACK) return true
            tg.answerCallbackQuery(callbackQueryId = callbackQuery.id)
            if (canRollback(ephemeralHook)) rollback(ephemeralHook)
            return false
        }
    }

    // (4) Handling initial message
    class Initial(private val verticle: RegisterVerticle) : State(verticle) {
        override suspend fun doHandleMessage(message: Message) {
            if (!message.isCommandForBot(CMD, typedConfig.me)) {
                die(DeathReason.FAILED)
                return
            }
            become(AskName(verticle), HistoryBehavior.SKIP)
        }
    }

    // (5) Handling response
    class AskName(private val verticle: RegisterVerticle) : State(verticle) {
        // (6) A side effect that is happening when you enter the state
        override suspend fun sideEffect() {
            // (7) sendOrEdit is a helper function to implement "in-place" responses for callback responses
            sendOrEdit(
                text = "Type your name".toFormattedText(),
                replyMarkup = inlineKeyboard {
                    row {
                        // (8) Button for going back
                        backButton(canRollback())
                    }
                }
            )
        }

        // (9) handling the name
        override suspend fun doHandleMessage(message: Message) {
            val name = message.text ?: return
            become(
                AskAge(
                    verticle = verticle,
                    name = name
                )
            )
        }
    }

    class AskAge(
        private val verticle: RegisterVerticle,
        private val name: String
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit(
                text = "Type your age".toFormattedText(),
                replyMarkup = inlineKeyboard {
                    row {
                        backButton(canRollback())
                    }
                }
            )
        }

        override suspend fun doHandleMessage(message: Message) {
            val age = message.text?.toIntOrNull() ?: return
            become(
                AskFavouriteColor(
                    verticle = verticle,
                    name = name,
                    age = age
                )
            )
        }
    }

    class AskFavouriteColor(
        private val verticle: RegisterVerticle,
        private val name: String,
        private val age: Int
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit(
                text = "Type or select your favourite color:".toFormattedText(),
                replyMarkup = inlineKeyboard {
                    row {
                        callback(
                            text = "\uD83D\uDFE2",
                            callbackData = "green"
                        )
                        callback(
                            text = "\uD83D\uDD34",
                            callbackData = "red"
                        )
                        callback(
                            text = "\uD83D\uDD35",
                            callbackData = "blue"
                        )
                        callback(
                            text = "⚫\uFE0F",
                            callbackData = "black"
                        )
                    }
                    row {
                        backButton(canRollback())
                    }
                }
            )
        }
        override suspend fun doHandleMessage(message: Message) {
            val color = message.text ?: return
            become(
                Persist(
                    verticle = verticle,
                    name = name,
                    age = age,
                    favouriteColor = color
                )
            )
        }

        override suspend fun doHandleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            val color = callbackQuery.data ?: return
            become(
                Persist(
                    verticle = verticle,
                    name = name,
                    age = age,
                    favouriteColor = color
                )
            )
        }
    }

    // (10) Final state
    class Persist(
        private val verticle: RegisterVerticle,
        private val name: String,
        private val age: Int,
        private val favouriteColor: String
    ) : State(verticle) {
        override suspend fun sideEffect() {
            persist()
            sendOrEdit(
                text = textMarkdown {
                    +"We've recorded your response"
                    br()
                    b {
                        +"Your name: "
                        +name
                        br()
                        +"Your age: "
                        +age.toString()
                        br()
                        +"Your favourite color: "
                        +favouriteColor
                        br()
                    }
                }
            )
            // (11) We're done
            complete()
        }
        
        // (12) Only pretending to persist
        suspend fun persist() {
            // TODO
        }
    }

    override val chatId: Long
        get() = typedConfig.chatId
    override val initialState: State
        get() = Initial(this)
    override val timeout: Duration?
        get() = Duration.ofMinutes(3)

    data class Config(
        val chatId: Long,
        val me: User.Me
    )

    companion object {
        const val CMD = "register"
    }
}

// (13) Dispatch verticle, pretty much similar to the one we've seen before
class RegisterDispatchVerticle : DispatchVerticle.ByChatAndUser<RegisterDispatchVerticle.Config, RegisterVerticle.Config>() {
    override suspend fun prepareChild(dialogKey: DialogKey, msg: Message): Deployment<RegisterVerticle.Config>? {
        if (!msg.isCommandForBot(RegisterVerticle.CMD, typedConfig.me))
            return null

        return Deployment(
            RegisterVerticle(),
            RegisterVerticle.Config(
                chatId = msg.chat.id,
                me = typedConfig.me,
            )
        )
    }

    data class Config(
        val me: User.Me,
        override val baseAddress: String = TelegramAddress.DEMUX_BASE,
        override val verticleAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE
    ) : DispatchVerticle.Config
}

fun main() {
    // (14) Deploying stuff, same as before
    Vertx.vertx().runBlocking {
        attachVertigram().apply {
            deployTelegramEnsemble(
                token = "xxx:yyy",
                allowedUpdates = listOf(Update.Type.MESSAGE, Update.Type.CALLBACK_QUERY)
            )
            val tg = ThinTelegram(vertigram = this@apply)
            val me = tg.getMe()
            deployVerticle(RegisterDispatchVerticle(), RegisterDispatchVerticle.Config(me))
        }
    }
}
```

Let's walk through it, step by step:
1. First we define a reply markup helper function that adds a client-owned back button to our reply markup.
2. Dialog logic is defined in a class inherited from `StatefulTelegramDialogVerticle`. Again, you can assume
that all interaction happens in the same dialog (as you defined it in dispatch verticle). Shared client state code handles
the back callback and invokes the framework rollback API.
3. Defining *states* for a dialog. Each state defines a part of dialog logic and inherited from `StatefulTelegramDialogVerticle.State`.
4. Handling the initial state which immediately transfers the dialog to the `AskName` state. `become` is a function to
perform a **state transition**. It accepts a new states an **optional** parameter for history behavior. Here we use
`SKIP` option, because we do not want our time-travel mechanism to return to initial state. In all other cases
we use the default behavior which records the history. 
5. Our first **state** will ask user for their name and handle the response.
6. First thing state "does" is executing a `sideEffect` function. This is a good place to ask user a question. 
7. Note the `sendOrEdit` function: first it does not have a `chatId`
parameter because it knows the chat id from the way we've overridden `chatId` property, second thing to note is its
behavior: it sometimes decides to edit a previous message (which it tracks itself), specifically when there is no
messages after it (e.g., when user has clicked an inline keyboard button). You can use it if you're fine with the logic
it implements, or you can implement your own logic on top of the telegram client.
8. Note the use of our helper from step 1. We're adding a row for the **back** function.
9. Here we are reading user's name from their response and transitioning to the next step: `AskAge`. Two next steps are
pretty similar.
10. Last state is `Persist`. Here we (pretend to) persist the data we've collected, sharing the feedback to the user and
`complete()`ing the dialog.
11. `complete()` completes the verticle successfully and freeing up the resources assouciated with it.
12. Note that we do not actually persist anything because it's is outside the scope of this tutorial. You can
implement any persistence logic you want here, consider using <a href="../vertigram-jooq/index.html">`vertigram-jooq`</a>
module to store the responses in a relational database.
13. **Dispatch verticle** is pretty much the same as we had before
4Deployment logic is also the same.

### Rollback

Rollback is invoked explicitly by application handlers. `canRollback(hook)` and `rollback(hook)` operate on the
immediately previous history entry. A regular target needs no hook; an ephemeral target requires a fresh non-null hook.
If that requirement is not met, rollback is unavailable and leaves history unchanged.

`canRollbackRegular()` and `rollbackRegular()` search backward for the latest regular `State`, skipping intervening
ephemeral entries. This variant never requires a hook. Vertigram does not reserve or intercept a back command or callback
value; applications own their controls and should use the matching check before invoking either rollback operation.
All rollback operations and availability checks require the dialog lock. Handlers, side effects, and `setTimer` callbacks
already hold it; calls from other coroutines must be wrapped in `withLock`.

### Handling Micro-State

While state transitions are good in case of changing some "big" state, sometimes the notation can be too verbose.
You can combine **state** mechanism with maintaining some state inside toyr state using class fields. Let's "improve"
UI of our `AskAge` stel by adding some buttons (yes, I understand that this is barely an improvement, but this is good
for demo purposes):
```kotlin
    class AskAge(
        private val verticle: RegisterVerticle,
        private val name: String,
    ) : State(verticle) {
        var age = DEFAULT_AGE

        private suspend fun proceed(age: Int = this.age) {
            become(
                AskFavouriteColor(
                    verticle = verticle,
                    name = name,
                    age = age
                )
            )
        }

        private suspend fun update() {
            sendOrEdit(
                text = "Type your age".toFormattedText(),
                replyMarkup = inlineKeyboard {
                    row {
                        if (age > MIN_AGE) {
                            callback(
                                text = "-",
                                callbackData = MINUS
                            )
                        }
                        callback(
                            text = "I am $age years old",
                            callbackData = OK
                        )
                        if (age < MAX_AGE) {
                            callback(
                                text = "+",
                                callbackData = PLUS
                            )
                        }
                    }
                    row {
                        backButton(canRollback())
                    }

                }
            )
        }

        override suspend fun sideEffect() {
            update()
        }

        override suspend fun doHandleMessage(message: Message) {
            val age = message.text?.toIntOrNull() ?: return
            proceed(age)
        }

        override suspend fun doHandleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            when (callbackQuery.data) {
                MINUS -> {
                    age--
                    update()
                }
                PLUS -> {
                    age++
                    update()
                }
                OK -> {
                    tg.answerCallbackQuery(
                        callbackQueryId = callbackQuery.id,
                    )
                    proceed()
                }
            }
        }

        companion object {
            const val PLUS = "+"
            const val MINUS = "-"
            const val OK = "OK"
            const val DEFAULT_AGE = 18
            const val MAX_AGE = 150
            const val MIN_AGE = 0
        }
    }
```

Here we handle some callback queries inside the state and store currently selected age as a class field. You can notice
that the back button still works as expected.

Alternatively you can treat `State` objects as immutable and handle callbacks with `become`, but in that case
you'd have to deal with history behavior, so your state history does not become too verbose 
(note the second argument to `become`):
```kotlin
    class AskAge(
        private val verticle: RegisterVerticle,
        private val name: String,
        private val age: Int = DEFAULT_AGE
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit(
                text = "Type your age".toFormattedText(),
                replyMarkup = inlineKeyboard {
                    row {
                        if (age > MIN_AGE) {
                            callback(
                                text = "-",
                                callbackData = MINUS
                            )
                        }
                        callback(
                            text = "I am $age years old",
                            callbackData = OK
                        )
                        if (age < MAX_AGE) {
                            callback(
                                text = "+",
                                callbackData = PLUS
                            )
                        }
                    }
                    row {
                        backButton(canRollback())
                    }

                }
            )
        }

        override suspend fun doHandleMessage(message: Message) {
            val age = message.text?.toIntOrNull() ?: return
            become(
                AskFavouriteColor(
                    verticle = verticle,
                    name = name,
                    age = age
                )
            )
        }

        override suspend fun doHandleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            when (callbackQuery.data) {
                MINUS -> {
                    become(
                        AskAge(
                            verticle = verticle,
                            name = name,
                            age = age - 1
                        ),
                        HistoryBehavior.SKIP
                    )
                }
                PLUS -> {
                    become(
                        AskAge(
                            verticle = verticle,
                            name = name,
                            age = age + 1
                        ),
                        HistoryBehavior.SKIP
                    )
                }
                OK -> {
                    tg.answerCallbackQuery(
                        callbackQueryId = callbackQuery.id,
                    )
                    become(
                        AskFavouriteColor(
                            verticle = verticle,
                            name = name,
                            age = age
                        )
                    )
                }
            }
        }

        companion object {
            const val PLUS = "+"
            const val MINUS = "-"
            const val OK = "OK"
            const val DEFAULT_AGE = 18
            const val MAX_AGE = 150
            const val MIN_AGE = 0
        }
    }
```

`EphemeralState` maintains an ephemeral message visible only to the user identified by its hook. Enter it from a handler
that has a hook by passing that hook to the ephemeral `become` overload:

```kotlin
become(ConfigureDialog(verticle), ephemeralHook)
```

The state installs the hook before its handler or side effect runs, so its implicit `sendOrEdit` targets the current
ephemeral message. Transition back to regular delivery with the regular overload:

```kotlin
become(StartPublicFlow(verticle))
```

Accepted ephemeral messages and callbacks refresh the installed hook for the duration of handling. Regular messages in
groups and supergroups are ignored by an `EphemeralState`; private-chat messages are adapted to ephemeral delivery.
`State` may also handle ephemeral input, but its `sendOrEdit` remains regular unless the hook is passed explicitly.
When no reply markup is provided for an ephemeral delivery, `ForceReply` is added automatically.

### Terminal States and Cancellation

Completion and cancellation are application behavior. Define client-owned terminal `State` and `EphemeralState`
implementations, render any desired response in `sideEffect`, and call `complete()` or `cancel()`. The framework does not
intercept cancel commands or callbacks; shared client base states are a convenient place to deduplicate that handling.

Silent timeout remains a core default for both delivery models. Override `timeoutState` and `ephemeralTimeoutState` to
provide application-specific timeout behavior and call `timeout()` from those terminal states.

### Timeouts

Unlike `SimpleTelegramDialogVerticle`, you don't have to implement timeout logic yourself. However, you have to opt in for
timeout handling by overriding the `timeout` field of the verticle:
```kotlin
    override val timeout: Duration?
        get() = Duration.ofMinutes(3)
```

This is an absolute timeout measured from deployment. User interaction does not reset it.

## Optional Logback integration

Vertigram includes an optional Logback appender that publishes log events on its event bus. Logback is not pulled into
applications transitively; add it explicitly when this integration is needed:

```kotlin
dependencies {
    implementation("ski.gagar.vertigram:vertigram:<version>")
    runtimeOnly("ch.qos.logback:logback-classic:<version>")
}
```

When using the published Vertigram version catalog, the second declaration can be written as
`runtimeOnly(vertigramLibs.logback.classic)`. Use `implementation` instead when application source code directly
references Logback types or constructs `EventBusAppender` programmatically.

Configure and attach `EventBusAppender` to the appropriate loggers in `logback.xml`:

```xml
<appender name="vertigram" class="ski.gagar.vertigram.logback.EventBusAppender">
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
        <level>WARN</level>
    </filter>
</appender>

<root level="INFO">
    <appender-ref ref="vertigram"/>
</root>
```

With the default `Vertigram.Config.initializers`, `attachVertigram()` discovers the Logback initializer and attaches
every configured `EventBusAppender` automatically. If an application supplies its own initializer list, include
`VertigramLogbackInitializer()` or call `attachEventBusLogging()` explicitly after creating Vertigram.

The default logical Vertigram address is `ski.gagar.vertigram.logback`. The raw Vert.x address is namespaced for the
Vertigram instance. The logical address can be changed through the appender's `address` property:

```xml
<appender name="vertigram" class="ski.gagar.vertigram.logback.EventBusAppender">
    <address>com.example.logs</address>
</appender>
```

Consumers receive `LogEvent` values. Logging inside such a consumer can create a feedback loop; wrap those records with
`bypassEventBusAppender` or `bypassEventBusAppenderSuspend` so the appender ignores them.

To forward events to a Telegram chat or channel, deploy `TelegramLoggingVerticle` after the standard Telegram ensemble:

```kotlin
vertigram.deployVerticle(
    TelegramLoggingVerticle(),
    TelegramLoggingVerticle.Config(chatId = -100123456)
)
```

For a custom appender address, pass the same value as `listenAddress`. If the Telegram ensemble uses a custom base
address, pass it as `telegramAddress`.
## What's Next?

Now you're familiar with using Telegram client from Vertigram and some basic building blocks for your bots.
You can optionally look into <a href="../vertigram-jooq/index.html">`vertigram-jooq`</a> module docs. This module is
more or less isolated from the rest of Vertigram and allows you to interact with relational database in an opinionated
way with the power of (Flyway and jOOQ).
