package ski.gagar.vxutil.web

import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpVersion
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.LoggerFormat
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.impl.Utils
import ski.gagar.vxutil.ip.IpAddress
import ski.gagar.vxutil.ip.IpNetworkAddress
import ski.gagar.vxutil.logger
import java.lang.AssertionError
import java.lang.IllegalArgumentException

// TODO simplify it
class RealIpFormatter(private val immediate: Boolean = false,
                      private val trustedNetworks: Set<IpNetworkAddress> = setOf(),
                      private val trustDomainSockets: Boolean = false) :
    java.util.function.Function<HttpServerRequest, String> {

    private val String?.shouldBeTrusted: Boolean
        get() {
            this ?: return trustDomainSockets

            return try {
                trustedNetworks.any { IpAddress(this) in it }
            } catch (ex: IllegalArgumentException) {
                logger.error("Failed to parse client IP", ex)
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

    override fun apply(request: HttpServerRequest): String {
        var contentLength: Long = 0
        contentLength = if (immediate) {
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
