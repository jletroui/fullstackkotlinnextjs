package org.example.app
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream
import kotlin.io.path.Path

@Serializable
data class Config(
    @Required @SerialName("_postgresUrl")       val postgresUrl: String,
    @Required @SerialName("_postgresAdminUser") val postgresAdminUser: String,
    @Required                                   val postgresAdminPassword: String,
    @Required @SerialName("_postgresAppUser")   val postgresAppUser: String,
    @Required                                   val postgresAppPassword: String,
) {
    companion object {
        private val encryptedEnvs = setOf("production", "staging")

        fun loadFromStdInOrDev(): Config {
            if (encryptedEnvs.contains(System.getenv("ENV"))) {
                // We're in prod or staging, docker entrypoint must decrypt secret file and pass it through StdIn.
                // Note: this is important so decrypted secrets leave no trace in env variables or on the file system.
                return fromStream(System.`in`)
            }
            // We're in dev, so we simply load dev secrets, which are not encrypted
            Path("backend", "secrets", "backend.dev.json").toFile().inputStream().use { stream ->
                return fromStream(stream)
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        fun fromStream(stream: InputStream): Config = Json.decodeFromStream(stream)
    }
}
