package ski.gagar.vxutil.web

import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpVersion
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerFormatter
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.impl.Utils
import ski.gagar.vxutil.lazy
import ski.gagar.vxutil.logger

// TODO simplify it
class RealIpFormatter(private val immediate: Boolean = false,
                      private val trustedNetworks: Set<IpNetworkAddress> = setOf(),
                      private val trustDomainSockets: Boolean = false) :
    LoggerFormatter {

    private val String?.shouldBeTrusted: Boolean
        get() {
            this ?: return trustDomainSockets

            return try {
                trustedNetworks.any { IpAddress(this) in it }
            } catch (ex: IllegalArgumentException) {
                logger.lazy.error(throwable = ex) { "Failed to parse client IP" }
                false
            }
        }

    private val HttpServerRequest.xRealIp
        get() = getHeader(X_REAL_IP)

    private val HttpServerRequest.xForwardedFor0
        get() = getHeader(X_FORWARDED_FOR)?.split(", ")?.firstOrNull()


    private val HttpServerRequest.clientSuppliedAddress
        get() = xRealIp ?: xForwardedFor0 ?: host()

    private val HttpServerRequest.clientAddress: String?
        get() {
            val host = remoteAddress().host()
            return if (host.shouldBeTrusted)
                clientSuppliedAddress
            else
                host()
        }

    override fun format(context: RoutingContext, ms: Long): String {
        val request: HttpServerRequest = context.request()
        val contentLength = if (immediate) {
            request.headers()["content-length"]?.toLongOrNull() ?: 0
        } else {
            request.response().bytesWritten()
        }

        val headers = request.headers()
        val status = request.response().statusCode
        val timestamp = System.currentTimeMillis()
        val remoteClient = request.clientAddress
        val method = request.method()
        val uri = request.uri()
        val referrer =
            if (headers.contains("referrer")) headers["referrer"] else headers["referer"] ?: "-"
        val userAgent = request.headers()["user-agent"] ?: "-"
        val versionFormatted = when (request.version()) {
            HttpVersion.HTTP_1_0 -> "HTTP/1.0"
            HttpVersion.HTTP_1_1 -> "HTTP/1.1"
            HttpVersion.HTTP_2 -> "HTTP/2.0"
            else -> "-"
        }


        return "$remoteClient - - [${Utils.formatRFC1123DateTime(timestamp)}] \"$method $uri $versionFormatted\" " +
                "$status $contentLength \"$referrer\" \"$userAgent\""
    }

    companion object {
        const val X_REAL_IP = "X-Real-Ip"
        const val X_FORWARDED_FOR = "X-Forwarded-For"
    }
}

fun RealIpLoggerHandler(immediate: Boolean = false,
                        trustedNetworks: Set<IpNetworkAddress> = setOf(),
                        trustDomainSockets: Boolean = false) =
    LoggerHandler.create(immediate, LoggerFormat.CUSTOM)
        .customFormatter(RealIpFormatter(immediate, trustedNetworks, trustDomainSockets))
