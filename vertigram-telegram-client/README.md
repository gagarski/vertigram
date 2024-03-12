# Module vertigram-telegram-client

`vertigram-telegram-client` module contains Kotlin coroutines-based wrapper for 
[Telegram Bot API](https://core.telegram.org/bots/api) built around 
[Vert.x Web Client](https://vertx.io/docs/vertx-web-client/java/).

It provides full and up-to-date implementation of Telegram Bot API using power of 
Kotlin programming language.

## Features

Besides Kotlin wrapper around Telegram API, `vertigram-telegram-client` provides the following 
features:
 - Better typing for Telegram types and methods. For example, you won't be able to call `editMessageText`
  method with both `inlineMessageId` and `chatId` set up.
 - Better structured type names. For example, `InlineQueryResultArticle` becomes `InlineQuery.Result.Article`.
  In most cases you will be able to figure out the types while writing the code with your IDE, however, the type 
  "renaming" policy is described in KDoc for each class.
 - Type-safe parameters for methods and types containing rich text-like data (`text` + `parse_mode` + `text_entities`).
 - Type-safe builders for message text markup and reply markup.

## Basic Usage

Telegram client can be created by creating an instance of [DirectTelegram](ski.gagar.vertigram.telegram.client.DirectTelegram) class,
which implements [Telegram](ski.gagar.vertigram.telegram.client.Telegram) interface. Telegram methods can be called by calling 
extension methods on [Telegram](ski.gagar.vertigram.telegram.client.Telegram) instance:

```kotlin
val vertx = Vertx.vertx()

runBlocking(vertx.dispatcher()) {
  val tg = DirectTelegram("111222333:secrettoken", vertx)

  val me = tg.getMe()

  println("Here starts ${me.username}!")
}
```

Note that the code is being run inside `runBlocking` coroutine builder. This happens because all Telegram methods are
implemented as `suspend`ing functions, which means that they can be run only inside coroutines. `runBlocking` is not the 
only place where you can run `Telegram` methods. You can user `Vertx.dispatcher()` method to get a dispatcher explicitly,
or you can use Vert.x `CoroutineVerticle` which implements `CoroutineScope`. `vertigram-core` also provides some
first-class support for coroutines.

There is a big chance that you want your bot to send some Telegram messages, let's try that:

```kotlin
tg.sendMessage(
    chatId = "@someusername".toChatId(),
    richText = "Hello World".toRichText(),
    disableNotifications = true
)
```

Here we see three parameters:
 - `chatId`. You can see `toChatId()` being called on a username. You can notice in Telegram API docs that in most cases
  the type of `chat_id` field is defined as `Integer or String`. `toChatId()` is a converter to type 
  [ski.gagar.vertigram.telegram.types.util.ChatId] which can store both and is properly serialized so Telegram Bot API
  server can understand it.
 - `richText` parameter replaces three original Telegram API parameters: `text`, `parse_mode` and `entities`. Having 
  one parameter instead of three prevents you from sending Markdown text with `parse_mode` set to `HTML`. In this example
  we're just passing plain text and converting it with `.toRichText()` to make the type match the signature. We'll come
  to sending formatted text later.
 - `disableNotifications` is a Telegram `disable_notifications` parameter.

You may notice that `camelCase` notation is used for parameters instean of `snake_case` used by Telegram. Even though,
this example shows some difference between Kotlin API and raw Telegram Bot API, this example covers most of the difference
there is. In most cases you can refer to Telegram Bot API docs and straightforwardly interpret the docs to 
`vertigram-telegram-client` API.

You also may notice that we've used **keyword argument** syntax to call the method. Because a lot of Telegram methods
have a lot of parameters, **positional arguments** syntax is forbidden for calling all the methods. This restriction may
be lifted a bit later for methods with small signature, but in general you're enforced to use keyword argument syntax:
this improves code readability when you have a lot of arguments and helps with code compatibility when upgrading to a 
new version. This restriction is enforced in all Telegram methods and type constructors and selectively in some
other functions (most of which are related to Telegram entities). In case you see a reference to 
[NoPosArgs](ski.gagar.vertigram.util.NoPosArgs) class, that means you're not allowed to use positional arguments here.

## Rich Text and Reply Markup

We've barely touched [toRichText](ski.gagar.vertigram.telegram.markup.toRichText) method in previous section. 
This method converts plain string to a `RichText`.

Telegram supports four formats for text formatting:
 - Markdown
 - Markdown V2
 - HTML
 - Text with entities

[RichText](ski.gagar.vertigram.telegram.types.richtext.RichText) subclasses support all of them and basically wrap 
text-related fields like `text`, `parse_mode` and `entities` to a single entity. Besides some weird wrappers for the 
part of method parameters, Vertigram gives you **type-safe** builders for formatted text:

```kotlin
tg.sendMessage(
    chatId = msg.chat.id.toChatId(),
    richText = textMarkdown {
        +"Hello"
        space()
        b {
            +"from"
            space()
            i {
                +"my"
                space()
                a(href="https://vertigram.gagar.ski") {
                    "Vertigram"
                }
                space()
                +"bot"
            }
      }
      br()
      +"More features are coming"
  }
)
```

You can see the following operations inside rich text markup:
 - Unary `+` yields a text.
 - `b`, `i` and `a` are bold, italic and link wrappers for text, they support nested entities.
 - `space()` and `br()` are just shortcuts for `+"  "` and `+"\n"` respectively.

[textMarkdown](ski.gagar.vertigram.telegram.markup.textMarkdown) is a builder producing 
[MarkdownV2Text](ski.gagar.vertigram.telegram.types.richtext.MarkdownV2Text) instance, 
representing a rich-text entity with Markdown V2 format. 
Note that Markdown V1 is considered deprecated and there is no builder for it. You can still use deprecated
[MarkdownText](ski.gagar.vertigram.telegram.types.richtext.MarkdownText) directly though. Besides,
[textMarkdown](ski.gagar.vertigram.telegram.markup.textMarkdown) there are 
[textHtml](ski.gagar.vertigram.telegram.markup.textHtml) and 
[textWithEntities](ski.gagar.vertigram.telegram.markup.textWithEntities) builders. They all support the same markup 
features and the only difference is the format of text passed to Telegram. You can use whichever you like the most.
For example, Markdown is more readable without rendering, so if you want to log sent messages somehow, you may prefer it
and text with entities might allow you to extend your message length limit and consistent with the text you get **from**
Telegram.

Telegram bots also often use **reply markup** to the messages. The following code will send a message with **inline keyboard**
reply markup:

```kotlin
tg.sendMessage(
    chatId = msg.chat.id.toChatId(),
    richText = "Look at those buttons".toRichText(),
    replyMarkup = inlineKeyboard {
        row {
            text(text = "Button 1")
            url(text = "Button 2", url = "https://example.com")
        }

        row {
            switchInline(text = "Button 3", switchInlineQuery = "bla")
            callback(text = "Button 4", callbackData = "qweqwe")
        }
    }
)
```

This example shows an inline keyboard with two rows having different kind of buttons.

The following builders for reply markup are supported (they cover all reply markup supported by Telegram):
 - [inlineKeyboard](ski.gagar.vertigram.telegram.markup.inlineKeyboard)
 - [keyboard](ski.gagar.vertigram.telegram.markup.keyboard)
 - [keyboardRemove](ski.gagar.vertigram.telegram.markup.keyboardRemove)
 - [forceReply](ski.gagar.vertigram.telegram.markup.forceReply)

## Sending Media

When it comes to sending media, Telegram allows you to either use media that exists on Telegram servers or somewhere 
on the Web, or upload a new one. Vertigram supports both these options.

```kotlin
tg.sendPhoto(
    chatId = "@someone",
    photo = "https://example.com/cat.jpg".toAttachment()
)
```

That code will send a hypothetical picture of a cat to `@someone`. Same approach can be applied to send a photo by a 
file id obtained from Telegram

```kotlin
tg.sendPhoto(
    chatId = "@someone",
    photo = "an_if_of_the_cat_photo_which_you_somehow_obtained_from_telegram".toAttachment()
)
```

Telegram servers will do their own magic to distinguish these two cases.

You can also upload a file from your local filesystem:

```kotlin
tg.sendPhoto(
    chatId = "@someone",
    photo = File("/home/cat_owner/cat.jpg").toAttachment()
)
```

[toAttachment](ski.gagar.vertigram.telegram.types.attachments.toAttachment) method converts [String](kotlin.String) and
[File](java.io.File) instances to [Attachment](ski.gagar.vertigram.telegram.types.attachments.Attachment) instance,
expected as a `photo`.

Vertigram currently supports two kind of attachments:
 - [StringAttachment](ski.gagar.vertigram.telegram.types.attachments.StringAttachment) representing either URL or file id, passed to Telegram
 - [FileAttachment](ski.gagar.vertigram.telegram.types.attachments.FileAttachment) representing a local FS attachment

While this might be not so much in Vert.x environment, [Attachment](ski.gagar.vertigram.telegram.types.attachments.Attachment)
is open for external extension. With a reasonable ammount of effort you can implement your own kind of attachments:
for example, database attachment or event-bus-streamed attachment. Follow the Javadoc to figure out the way to extend it
if you're up to it.

Other media-producing methods follow the similar idea, yet 
[sendMediaGroup](ski.gagar.vertigram.telegram.methods.sendMediaGroup) supports multiple media and therefore is worth
additional mentioning. The idea is pretty much the same, yet `Attachment`s are wrapped into `media` list:
```kotlin
tg.sendMediaGroup(
    chatId = "@someone".toChatId(),
    media = listOf(
        InputMedia.Photo(
            media = File("/home/cat_owner/cat.jpg").toAttachment(),
            richCaption = "A cat from local FS".toRichText()
        ),
        InputMedia.Photo(
            media = File("/home/cat_owner/cat.jpg").toAttachment(),
            richCaption = "A cat from web".toRichText()
        )
    )
)
```

## Differences from Raw Telegram API

While Vertigram tries to generally follow the Telegram REST API, there are a few differences which would allow you to 
write more concise and Kotlin-style code:
 - Use of Kotlin naming conventions (`camelCase` for method parameters, `UPPERCASE_SNAKE_CASE` for enum items)
 - Type-safe rich-text
 - Own way of typing attachments
 - Some types renamed for more structured naming and split into multiple cases
 - `sendPoll` method is split into `sendPoll` for regular poll and `sendQuiz` to provide safer arguments and better
   semantics to distinguish them
 - `close` is renamed to `closeApi` to not interfere with `AutoCloseable`
 - `getUpdates` lacks `timeout` parameter, timeouts are managed globally across 
   [DirectTelegram](ski.gagar.vertigram.telegram.client.DirectTelegram) instance.

## What's Next?

`vertigram-telegram-client` gives you a wrapper around Telegram Bots API. If that's all what you want, you can go from 
here. Note that [DirectTelegram](ski.gagar.vertigram.telegram.client.DirectTelegram) should be considered a heavy object,
it holds HTTP connection pools and if you're going to create it all the way, you'll pretty soon hit Telegram connection 
limits. Vert.x gives you a plenty of way of sharing an object locally (for example with 
[sharedData](io.vertx.core.Vertx.sharedData) or just by passing it everywhere as a parameter). 
[DirectTelegram](ski.gagar.vertigram.telegram.client.DirectTelegram) is safe to be shared and can be used from multiple 
threads. However, if you want to explore Vertigram way of sharing [Telegram](ski.gagar.vertigram.telegram.client.Telegram)
across your bot, you may explore `vertigram` module, which introduce `vertigram-core`-enabled way to talk to Telegram
from across your application. It wraps [Telegram](ski.gagar.vertigram.telegram.client.Telegram) into a verticle and
provides a thin client to talk to this verticle using event bus, providing same [Telegram](ski.gagar.vertigram.telegram.client.Telegram)
interface.