package org.example.app.helpers

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TestInit : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        context
            .root
            .getStore(ExtensionContext.Namespace.GLOBAL)
            .getOrComputeIfAbsent("init") {
                // Make sure the DB is up-to-date for tests.
                Flyway
                    .configure()
                    .dataSource("jdbc:postgresql://localhost/backend_test?ssl=false", "postgres", "secret")
                    .load()
                    .migrate()
                // should be 'this' if we want this to be a CloseableResource and a close() method to be called after all tests.
                "done"
            }
    }
}
