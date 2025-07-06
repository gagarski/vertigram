# Module vertigram-logback

`vertigram-logback` provides you some small building blocks to publish log events on EventBus through logback and
publish the logs to a telegram chat.

## Getting Started

First, you need to add an appender to your `logback.xml`:
```xml
<appender name="vertx" class="ski.gagar.vertigram.logback.EventBusAppender">
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
        <level>WARN</level>
    </filter>
</appender>
```

The usage of this appender is the same as for any other kind of appender: you probably need to attach it to a logger.
If you're familiar with basic logback concepts, this should not be a problem to you.

The vertigram address used to publish log events is `ski.gagar.vertigram.logback` and as of now cannot be changed.

Also, to start publishing log events you need to attach the appender to `Vertigram` instance. This can be done by a simple call:
```kt
    vertigram.attachEventBusLogging()
```

After that you should be able to listen on the log publishing address for [ski.gagar.vertigram.logback.LogEvent] messages.

## Avoiding loops

It might be tempting to use logging in your `ski.gagar.vertigram.logback` consumer. You should be careful about it,
since it will produce more log events which will eventually come back to your consumer again 
(potentially producing more log events).

You can deal with this by either carefully logging (e.g., producing only INFO records and setting up the filter in `logback.xml`).

Another way is to use convenience methods in your handler:
```kt
bypassEventBusAppenderSuspend {
    log.error("An error that won't pop up on event bus")
}
 // ... or ...
bypassEventBusAppenderSuspend {
    log.error("An error that won't pop up on event bus")
}
```

`bypass...` convenience functions set MDC context for the log records produced inside the lambda, 
telling `EventBusAppender` not to publish it on event bus.

## Telegram Logging Verticle

`vertigram-logback` also provides you with an option to publish your log events to a private Telegram channel or
personally to you. To do this, call:
```kt
vertigram.enableTelegramLogging(TelegramLoggingVerticle.Config(chatId = -100123456))
```

This will deploy `TelegramLoggingVerticle`, which relies on standard Telegram Verticle ensemble being deployed 
to publish messages.