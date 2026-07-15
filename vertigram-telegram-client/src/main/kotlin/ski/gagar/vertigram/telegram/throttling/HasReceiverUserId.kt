package ski.gagar.vertigram.telegram.throttling

/**
 * Implemented by calls which can target a specific receiver with an ephemeral message.
 */
interface HasReceiverUserId {
    val receiverUserId: Long?
}
