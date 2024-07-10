package org.example.app.config

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import org.slf4j.LoggerFactory

class WebServer {
    companion object {
        private val logger = LoggerFactory.getLogger(WebServer::class.java)

        fun start(config: Config, vertx: Vertx, installRoutes: (Router) -> Unit) {
            val router = Router.router(vertx)

            // Configure Middleware
            router
                .route()
                .handler(StructuredAccessLogHandler())

            // Configure API routes
            installRoutes(router)

            // Launch actual server
            vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(config.port)
                .onSuccess {
                    logger.atInfo().log("Running on port {}", config.port)
                }
        }
    }
}
