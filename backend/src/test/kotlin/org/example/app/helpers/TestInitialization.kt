package org.example.app.helpers

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TestInitialization : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        val config = Services.config
        context
            .root
            .getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrComputeIfAbsent("init") {
                // Make sure the DB is up-to-date for tests
                Flyway
                    .configure()
                    .dataSource(
                        "jdbc:postgresql://${config.postgresHost}/${config.postgresDatabase}?ssl=false",
                        config.postgresAdminUser,
                        config.postgresAdminPassword,
                    )
                    .load()
                    .migrate()
                // should be 'this' if we want this to be a CloseableResource and a close() method to be called after all tests.
                "done"
            }
    }
}
