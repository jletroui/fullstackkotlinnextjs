package org.example.app.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConfigTest {
    @Test
    fun testDeserializeDevConfig() {
        val sampleConfig =
            """
                {
                    "_port": 1234,
                    "_postgresHost": "localhost",
                    "_postgresDatabase": "backend",
                    "_postgresAdminUser": "postgres",
                    "postgresAdminPassword": "secret",
                    "_postgresAppUser": "backend",
                    "postgresAppPassword": "backend",
                    "unknownKey": "shouldBeIgnored"
                }
            """

        val expectedConfig =
            Config(
                1234,
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
