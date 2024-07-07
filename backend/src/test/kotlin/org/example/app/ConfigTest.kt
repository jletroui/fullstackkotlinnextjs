package org.example.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConfigTest {
    @Test
    fun testDeserializeDevConfig() {
        val sampleConfig = """
            {
                "_postgresUrl": "jdbc:postgresql://localhost/backend?ssl=false",
                "_postgresAdminUser": "postgres",
                "postgresAdminPassword": "secret",
                "_postgresAppUser": "backend",
                "postgresAppPassword": "backend"
            }
        """
        val expectedConfig = Config(
            "jdbc:postgresql://localhost/backend?ssl=false",
            "postgres",
            "secret",
            "backend",
            "backend",
        )
        assertEquals(expectedConfig, Config.fromStream(sampleConfig.byteInputStream()))
    }
}