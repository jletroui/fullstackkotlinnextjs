package org.example.app.web

import io.vertx.ext.web.Router
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.example.app.logic.TaskRepository

class TaskRoutes {
    companion object {
        fun install(
            router: Router,
            taskRepository: TaskRepository,
        ) {
            router
                .get("/api/tasks/count")
                .respond {
                    taskRepository.taskCount().map { count ->
                        json { obj("count" to count) }
                    }
                }
        }
    }
}
