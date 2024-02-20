package ski.gagar.vertigram.tools.verticles

import kotlinx.coroutines.yield
import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.jackson.suspendJsonConsumer
import ski.gagar.vertigram.tools.verticles.address.VertigramAddress
import ski.gagar.vertigram.types.ParsedUpdateList
import ski.gagar.vertigram.verticles.ErrorLoggingCoroutineVerticle
import ski.gagar.vertigram.verticles.WebHookVerticle

class UpdateDemuxVerticle : ErrorLoggingCoroutineVerticle() {
    private val typedConf: Config by lazy {
        config.mapTo()
    }
    override suspend fun start() {
        suspendJsonConsumer<ParsedUpdateList, Unit>(typedConf.addresses.listen) { dispatch(it) }
    }

    private suspend fun dispatch(list: ParsedUpdateList) {
        val conf = typedConf
        for (u in list) {

//            if (conf.addresses.message != null && u.message != null) {
//                vertx.eventBus().publishJson(conf.addresses.message, u.message)
//            }
//
//            if (conf.addresses.channelPost != null && u.channelPost != null) {
//                vertx.eventBus().publishJson(conf.addresses.channelPost, u.channelPost)
//            }
//
//            if (conf.addresses.inlineQuery != null && u.inlineQuery != null) {
//                vertx.eventBus().publishJson(conf.addresses.inlineQuery, u.inlineQuery)
//            }
//
//            if (conf.addresses.callbackQuery != null && u.callbackQuery != null) {
//                vertx.eventBus().publishJson(conf.addresses.callbackQuery, u.callbackQuery)
//            }
//
//            if (conf.addresses.chatMember != null && u.chatMember != null) {
//                vertx.eventBus().publishJson(conf.addresses.chatMember, u.chatMember)
//            }
//
//            if (conf.addresses.myChatMember != null && u.myChatMember != null) {
//                vertx.eventBus().publishJson(conf.addresses.myChatMember, u.myChatMember)
//            }
//
//            if (conf.addresses.pollAnswer != null && u.pollAnswer != null) {
//                vertx.eventBus().publishJson(conf.addresses.pollAnswer, u.pollAnswer)
//            }
//
//            if (conf.addresses.update != null) {
//                vertx.eventBus().publishJson(conf.addresses.update, u)
//            }

            yield()
        }
    }

    data class Config(
        val addresses: Addresses = Addresses()
    ) {
        data class Addresses(
            val listen: String = WebHookVerticle.Config.DEFAULT_UPDATE_PUBLISHING_ADDRESS,
            val message: String? = VertigramAddress.Message,
            val channelPost: String? = VertigramAddress.ChannelPost,
            val inlineQuery: String? = VertigramAddress.InlineQuery,
            val callbackQuery: String? = VertigramAddress.CallbackQuery,
            val chatMember: String? = VertigramAddress.ChatMember,
            val myChatMember: String? = VertigramAddress.MyChatMember,
            val pollAnswer: String? = VertigramAddress.PollAnswer,
            val update: String? = VertigramAddress.Update,
        )
    }
}
