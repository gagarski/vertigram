package ski.gagar.vertigram.tools

import ski.gagar.vertigram.types.Message

val Message.isForwarded
    get() = forwardOrigin != null
