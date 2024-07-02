plugins {
    // Apply the foojay-resolver settings plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include("backend", "frontend")
