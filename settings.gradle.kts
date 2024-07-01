pluginManagement {
    plugins {
        id("org.siouan.frontend-jdk21") version "8.1.0"
        id("org.jetbrains.kotlin.jvm") version "2.0.0"
    }
}

plugins {
    // Apply the foojay-resolver settings plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("backend", "frontend")
