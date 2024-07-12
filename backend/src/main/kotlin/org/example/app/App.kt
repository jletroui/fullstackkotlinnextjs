package org.example.app

import io.vertx.core.Vertx
import org.example.app.config.Config
import org.example.app.config.Database
import org.example.app.config.WebServer
import org.example.app.logic.PostgresTaskRepository
import org.example.app.web.TaskRoutes
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("main")
    val config = Config.loadFromStdInOrDev()
    Database
        .createFlyway(config)
        .migrate()

    val vertx = Vertx.vertx()

    val sqlClient = Database.createSqlClient(config, vertx)
    val taskRepository = PostgresTaskRepository(sqlClient)
    // Add some other repositories here

    WebServer.start(config, vertx) { router ->
        TaskRoutes.install(router, taskRepository)
        // Add some other controllers here
    }

    Runtime.getRuntime().addShutdownHook(
        Thread {
            try {
                logger.atInfo().log("Exiting...")
            } finally {
                // TODO: actually wait the future to stop before exiting this thread.
                vertx.close()
            }
        },
    )
}
