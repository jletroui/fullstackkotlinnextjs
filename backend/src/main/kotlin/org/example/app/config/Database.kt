package org.example.app.config

import io.vertx.core.Vertx
import io.vertx.pgclient.PgBuilder
import io.vertx.pgclient.PgConnectOptions
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.Socket

class Database {
    companion object {
        private const val WAIT_TIME_MS = 100L
        private val logger = LoggerFactory.getLogger(Database::class.java)

        private fun jdbcUrl(config: Config): String {
            return "jdbc:postgresql://${config.postgresHost}/${config.postgresDatabase}?ssl=false"
        }

        private fun waitDbReady(config: Config) {
            if (isDbReady(config)) {
                return
            }
            logger.atInfo().log(
                "Postgres server at ${config.postgresHost} is not responding. Waiting for {} ms.",
                config.postgresWaitTimeoutMs,
            )

            var tries = config.postgresWaitTimeoutMs / WAIT_TIME_MS
            while (!isDbReady(config) && tries > 0) {
                Thread.sleep(WAIT_TIME_MS)
                tries--
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
            } catch (_: IOException) {
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
