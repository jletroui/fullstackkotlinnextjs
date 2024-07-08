package org.example.app.config

import io.vertx.core.Vertx
import io.vertx.pgclient.PgBuilder
import io.vertx.pgclient.PgConnectOptions
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import org.flywaydb.core.Flyway

class DatabaseClientBuilder {
    companion object {
        private fun jdbcUrl(config: Config) = "jdbc:postgresql://${config.postgresHost}/${config.postgresDatabase}?ssl=false"

        fun createFlyway(config: Config): Flyway =
            Flyway
                .configure()
                .dataSource(jdbcUrl(config), config.postgresAdminUser, config.postgresAdminPassword)
                .load()

        fun createSqlClient(
            config: Config,
            vertx: Vertx,
        ): SqlClient {
            val connectOptions =
                PgConnectOptions()
                    .setHost(config.postgresHost)
                    .setDatabase(config.postgresDatabase)
                    .setUser(config.postgresAppUser)
                    .setPassword(config.postgresAppPassword)

            val poolOptions =
                PoolOptions()
                    .setMaxSize(5)

            return PgBuilder
                .client()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(vertx)
                .build()
        }
    }
}
