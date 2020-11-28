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
import java.lang.IllegalArgumentException

class LoggerHandler(private val immediate: Boolean = false,
                    private val format: LoggerFormat = LoggerFormat.DEFAULT,
                    private val trustedNetworks: Set<IpNetworkAddress> = setOf(),
                    private val trustDomainSockets: Boolean = false
) : LoggerHandler {

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
        get() = getHeader("X-Real-Ip")

    private val HttpServerRequest.xForwardedFor0
        get() = getHeader("X-Forwarded-For")?.split(", ")?.firstOrNull()


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

    private fun log(
        context: RoutingContext,
        timestamp: Long,
        remoteClient: String?,
        version: HttpVersion,
        method: HttpMethod,
        uri: String
    ) {
        val request = context.request()
        var contentLength: Long = 0
        contentLength = if (immediate) {
            request.headers()["content-length"]?.toLongOrNull() ?: 0
        } else {
            request.response().bytesWritten()
        }
        val versionFormatted = when (version) {
            HttpVersion.HTTP_1_0 -> "HTTP/1.0"
            HttpVersion.HTTP_1_1 -> "HTTP/1.1"
            HttpVersion.HTTP_2 -> "HTTP/2.0"
            else -> "-"
        }
        val headers = request.headers()
        val status = request.response().statusCode
        val message = when (format) {
            LoggerFormat.DEFAULT -> {
                // as per RFC1945 the header is referer but it is not mandatory some implementations use referrer
                var referrer =
                    if (headers.contains("referrer")) headers["referrer"] else headers["referer"]
                var userAgent = request.headers()["user-agent"]
                referrer = referrer ?: "-"
                userAgent = userAgent ?: "-"
                "$remoteClient - - [${Utils.formatRFC1123DateTime(timestamp)}] \"$method $uri $versionFormatted\" " +
                        "$status $contentLength \"$referrer\" \"$userAgent\""
            }
            LoggerFormat.SHORT ->
                "$remoteClient - $method $uri $versionFormatted $status $contentLength - " +
                        "${System.currentTimeMillis() - timestamp}"
            LoggerFormat.TINY -> "$method $uri $status $contentLength ${System.currentTimeMillis() - timestamp}"
        }
        doLog(status, message)
    }

    private fun doLog(status: Int, message: String?) {
        when {
            status >= 500 -> logger.error(message)
            status >= 400 -> logger.warn(message)
            else -> logger.info(message)
        }
    }

    override fun handle(context: RoutingContext) {
        // common logging data
        val timestamp = System.currentTimeMillis()
        val remoteClient = context.request().clientAddress
        val method = context.request().method()
        val uri = context.request().uri()
        val version = context.request().version()
        if (immediate) {
            log(context, timestamp, remoteClient, version, method, uri)
        } else {
            context.addBodyEndHandler {
                log(
                    context,
                    timestamp,
                    remoteClient,
                    version,
                    method,
                    uri
                )
            }
        }
        context.next()
    }

    companion object {
        const val X_REAL_IP = "X-Real-Ip"
        const val X_FORWARDED_FOR = "X-Forwarded-For"
    }
}
