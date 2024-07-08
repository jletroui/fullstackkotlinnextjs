package org.example.app

import io.vertx.core.Vertx
import org.example.app.config.Config
import org.example.app.config.DatabaseClientBuilder
import org.example.app.config.RouterBuilder
import org.example.app.logic.PostgresTaskRepository
import org.example.app.web.TaskRoutes

fun main() {
    val config = Config.loadFromStdInOrDev()
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
        .listen(8080)
}
