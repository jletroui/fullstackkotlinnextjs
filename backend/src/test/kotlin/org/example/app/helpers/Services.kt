package org.example.app.helpers

import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.sqlclient.SqlClient
import org.example.app.Config
import org.example.app.DatabaseClientBuilder

class Services {
    companion object {
        val config = Config.loadTestConfig()

        fun <T> withSqlClient(
            vertx: Vertx,
            f: (SqlClient) -> Future<T>,
        ): Future<T> {
            val client = DatabaseClientBuilder.createSqlClient(config, vertx)
            return f.invoke(client).map { item ->
                client.close()
                item
            }
        }
    }
}
