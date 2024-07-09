package org.example.app

import io.vertx.core.Vertx
import io.vertx.kotlin.core.closeAwait
import org.example.app.config.Config
import org.example.app.config.DatabaseClientBuilder
import org.example.app.config.RouterBuilder
import org.example.app.logic.PostgresTaskRepository
import org.example.app.web.TaskRoutes
import org.slf4j.LoggerFactory

fun main() {
    val config = Config.loadFromStdInOrDev()
    val logger = LoggerFactory.getLogger("main")
    DatabaseClientBuilder
        .createFlyway(config)
        .migrate()

    val vertx = Vertx.vertx()

    val sqlClient = DatabaseClientBuilder.createSqlClient(config, vertx)
    val taskRepository = PostgresTaskRepository(sqlClient)
    // Add some other repositories here

    val router = RouterBuilder.router(vertx)
    TaskRoutes.install(router, taskRepository)
    // Add some other controllers here

    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080) // TODO: move this port into config

    Runtime.getRuntime().addShutdownHook(
        Thread {
            try {
                logger.atInfo().log("Exiting...")
            }
            finally {
                // TODO: actually wait the future to stop before exiting this thread.
                vertx.close()
            }
        },
    )
}
