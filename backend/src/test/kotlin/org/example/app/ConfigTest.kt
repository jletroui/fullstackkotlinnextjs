package org.example.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConfigTest {
    @Test
    fun testDeserializeDevConfig() {
        val sampleConfig =
            """
                {
                    "_postgresHost": "localhost",
                    "_postgresDatabase": "backend",
                    "_postgresAdminUser": "postgres",
                    "postgresAdminPassword": "secret",
                    "_postgresAppUser": "backend",
                    "postgresAppPassword": "backend"
                }
            """

        val expectedConfig =
            Config(
                "localhost",
                "backend",
                "postgres",
                "secret",
                "backend",
                "backend",
            )
        assertEquals(expectedConfig, Config.fromStream(sampleConfig.byteInputStream()))
    }
}
