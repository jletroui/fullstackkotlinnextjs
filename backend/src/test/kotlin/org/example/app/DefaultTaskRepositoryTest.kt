package org.example.app

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.example.app.helpers.Services
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
class DefaultTaskRepositoryTest {
    @Test
    fun whenNoTaskAndTaskCountThenReturn0(
        vertx: Vertx,
        ctx: VertxTestContext,
    ) {
        usingRepo(vertx) { sut ->
            sut.taskCount().onComplete(ctx.succeeding { count ->
                assertEquals(0, count)
                ctx.completeNow()
            })
        }
    }

    @Test
    fun whenSomeTaskAndTaskCountThenReturnActualCount(
        vertx: Vertx,
        ctx: VertxTestContext,
    ) {
        usingRepo(vertx) { sut ->
            sut
                .createTask("a task")
                .compose {
                    sut.createTask("an other task")
                }
                .compose {
                    sut.taskCount()
                }
                .onComplete(ctx.succeeding { count ->
                    assertEquals(2, count)
                    ctx.completeNow()
                })
        }
    }

    companion object {
        fun <T> usingRepo(vertx: Vertx, f: (DefaultTaskRepository) -> Future<T>) =
            Services.withSqlClient(vertx) { client ->
                client
                    .query("TRUNCATE tasks")
                    .execute()
                    .compose {
                        val sut = DefaultTaskRepository(client)
                        f.invoke(sut)
                    }
            }
    }
}
