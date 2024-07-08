package org.example.app.config

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

/**
 * Configure the Vertx router for shared behaviour: content type, error handling, auth, etc...
 */
class RouterBuilder {
    companion object {
        fun router(vertx: Vertx): Router {
            val router = Router.router(vertx)
            router
                .route()
                .handler(StructuredAccessLogHandler())
            return router
        }
    }
}
