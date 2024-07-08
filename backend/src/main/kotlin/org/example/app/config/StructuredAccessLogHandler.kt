package org.example.app.config

import io.vertx.core.Handler
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpVersion
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.impl.Utils
import org.slf4j.LoggerFactory

/**
 * io.vertx.ext.web.handler.impl.LoggerHandlerImpl is logging as a text message only.
 * This is providing a structured access log instead.
 */
class StructuredAccessLogHandler : Handler<RoutingContext> {
    private val logger = LoggerFactory.getLogger(StructuredAccessLogHandler::class.java)

    override fun handle(ctx: RoutingContext) {
        val timestamp: Long = System.currentTimeMillis()
        val remoteClient: String = ctx.request().remoteAddress()?.host() ?: "-"
        val method: HttpMethod = ctx.request().method()
        val uri: String = ctx.request().uri()
        val version: HttpVersion = ctx.request().version()

        ctx.addBodyEndHandler {
            log(ctx, timestamp, remoteClient, version, method, uri)
        }

        ctx.next()
    }

    private fun log(
        context: RoutingContext,
        timestamp: Long,
        remoteClient: String,
        version: HttpVersion,
        method: HttpMethod,
        uri: String,
    ) {
        val request = context.request()
        val contentLength = request.response().bytesWritten()
        val versionFormatted =
            when (version) {
                HttpVersion.HTTP_1_0 -> "HTTP/1.0"
                HttpVersion.HTTP_1_1 -> "HTTP/1.1"
                HttpVersion.HTTP_2 -> "HTTP/2.0"
                else -> "-"
            }
        val headers = request.headers()
        val status = request.response().statusCode
        // as per RFC1945 the header is referer, but it is not mandatory some implementations use referrer
        val referer = if (headers.contains("referrer")) headers["referrer"] else headers["referer"] ?: "-"
        val userAgent = request.headers()["user-agent"] ?: "-"

        // "0:0:0:0:0:0:0:1 - - [Mon, 8 Jul 2024 18:19:27 GMT] \"GET /tasks/count HTTP/1.1\" 200 11 \"-\" \"curl/8.7.1\""
        val logBuilder =
            when {
                status >= 500 -> logger.atError()
                status >= 400 -> logger.atWarn()
                else -> logger.atInfo()
            }

        // https://github.com/logfellow/logstash-logback-encoder/tree/logstash-logback-encoder-7.4?tab=readme-ov-file#standard-fields-1
        logBuilder
            .addKeyValue("timestamp", Utils.formatRFC1123DateTime(timestamp))
            .addKeyValue("method", method.name())
            .addKeyValue("remote_host", remoteClient)
            .addKeyValue("requested_uri", uri)
            .addKeyValue("protocol", versionFormatted)
            .addKeyValue("status_code", status)
            .addKeyValue("content_length", contentLength)
            .addKeyValue("referer", referer)
            .addKeyValue("user_agent", userAgent)
            .log()
    }
}
