package org.example.app

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

/**
 * Configure the Vertx router for shared behaviour: content type, error handling, auth, etc...
 */
class RouterBuilder {
    companion object {
        fun router(vertx: Vertx): Router {
            val router = Router.router(vertx)
            // Seems JSON is already the default for simple routes
//            router
//                .route()
//                .produces("application/json")
//                .handler(ResponseContentTypeHandler.create())
            return router
        }
    }
}
