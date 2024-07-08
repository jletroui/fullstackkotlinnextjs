package org.example.app.logic

import io.vertx.core.Future
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple
import java.util.UUID

interface TaskRepository {
    fun taskCount(): Future<Int>

    fun createTask(description: String): Future<Unit>
}

class PostgresTaskRepository(private val sqlClient: SqlClient) : TaskRepository {
    override fun taskCount(): Future<Int> =
        sqlClient
            .query("SELECT COUNT(1) FROM tasks")
            .execute()
            .map { rs ->
                rs.first().getInteger(0)
            }

    override fun createTask(description: String): Future<Unit> =
        sqlClient
            .preparedQuery("INSERT INTO tasks (task_id, task_description) VALUES ($1, $2)")
            .execute(Tuple.of(UUID.randomUUID().toString(), description))
            .map(Unit)
}
