package org.example.app

import org.flywaydb.core.Flyway
import java.sql.DriverManager

fun main() {
    val config = Config.loadFromStdInOrDev()
    Flyway
        .configure()
        .dataSource(config.postgresUrl, config.postgresAdminUser, config.postgresAdminPassword)
        .load()
        .migrate()

    DriverManager.getConnection(config.postgresUrl, config.postgresAppUser, config.postgresAppPassword).use { conn ->
        conn.createStatement().use { stmt ->
            stmt.executeQuery("SELECT COUNT(1) FROM tasks").use { rs ->
                while (rs.next()) {
                    print("Column 1 returned ")
                    println(rs.getInt(1))
                }
            }
        }
    }
}
