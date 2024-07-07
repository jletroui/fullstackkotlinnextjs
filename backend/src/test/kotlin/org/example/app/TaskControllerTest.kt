package org.example.app

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import org.example.app.helpers.TestClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


@ExtendWith(VertxExtension::class)
class TaskControllerTest {
    @Test
    fun whenGetTaskCountThenActualCount(vertx: Vertx, ctx: VertxTestContext) {
        whenever(taskRepository.taskCount()).thenReturn(Future.succeededFuture(2))

        TestClient
            .get(vertx, "/tasks/count")
            .onComplete(ctx.succeeding { resp ->
                assertEquals(200, resp.statusCode())
                assertEquals(json { obj("count" to 2) }, resp.body())
                ctx.completeNow()
            })
    }

    companion object {
        private val taskRepository: TaskRepository = mock()

        @JvmStatic
        @BeforeAll
        fun init(vertx: Vertx, ctx: VertxTestContext): Unit {
            val router = Router.router(vertx)
            TaskController.installRoutes(router, taskRepository)
            vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(8080)
                .onComplete(ctx.succeedingThenComplete())

        }
    }
}
