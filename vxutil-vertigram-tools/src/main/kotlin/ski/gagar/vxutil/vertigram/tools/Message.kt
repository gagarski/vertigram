package ski.gagar.vxutil.vertigram.tools

import ski.gagar.vxutil.vertigram.types.Message

val Message.isForwarded
    get() = forwardOrigin != null
