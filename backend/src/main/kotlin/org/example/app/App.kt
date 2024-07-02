package org.example.app

import java.sql.DriverManager
import org.flywaydb.core.Flyway

fun main() {
    Flyway
        .configure()
        .dataSource("jdbc:postgresql://localhost/backend?ssl=false", "postgres", "secret")
        .load()
        .migrate()

    val url = "jdbc:postgresql://localhost/backend?user=postgres&password=secret&ssl=false"
    DriverManager.getConnection(url).use { conn ->
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
