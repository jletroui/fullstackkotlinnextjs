package org.example.app.config

import io.vertx.core.Vertx
import io.vertx.pgclient.PgBuilder
import io.vertx.pgclient.PgConnectOptions
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import org.flywaydb.core.Flyway
import java.io.IOException
import java.net.Socket

class Database {
    companion object {
        private fun jdbcUrl(config: Config) = "jdbc:postgresql://${config.postgresHost}/${config.postgresDatabase}?ssl=false"

        private fun waitDbReady(config: Config) {
            var maxTries = 20 // 2 seconds
            while (!isDbReady(config) && maxTries > 0) {
                Thread.sleep(100)
                maxTries--
            }
            if (!isDbReady(config)) {
                throw IllegalStateException("Postgres server at ${config.postgresHost} is not responding after 2s.")
            }
        }

        private fun isDbReady(config: Config): Boolean =
            try {
                Socket(config.postgresHost, 5432).use {
                    true
                }
            } catch (_: IOException){
                false
            }


        fun createFlyway(config: Config): Flyway {
            waitDbReady(config)
            return Flyway
                .configure()
                .dataSource(jdbcUrl(config), config.postgresAdminUser, config.postgresAdminPassword)
                .load()
        }

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
