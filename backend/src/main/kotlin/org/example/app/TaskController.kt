package org.example.app

import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

class TaskController {
    companion object {
        fun installRoutes(
            router: Router,
            taskRepository: TaskRepository,
        ) {
            router
                .get("/tasks/count")
                .respond {
                    taskRepository.taskCount().map { count ->
                        json { obj("count" to count) }
                    }
                }
        }
    }
}
