package org.example.app

import io.vertx.core.Vertx

fun main() {
    val config = Config.loadFromStdInOrDev()
    DatabaseClientBuilder
        .createFlyway(config)
        .migrate()

    val vertx = Vertx.vertx()

    val sqlClient = DatabaseClientBuilder.createSqlClient(config, vertx)
    val taskRepository = DefaultTaskRepository(sqlClient)
    // Add some other repositories here

    val router = RouterBuilder.router(vertx)
    TaskController.installRoutes(router, taskRepository)
    // Add some other controllers here

    vertx.createHttpServer().requestHandler(router).listen(8080)
}
