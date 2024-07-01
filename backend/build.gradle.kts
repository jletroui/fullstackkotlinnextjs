plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

java {
    // Apply a specific Java toolchain to ease working on different environments.
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

application {
    // Define the main class for the application.
    mainClass = "org.example.app.AppKt"
}

dependencies {
    constraints {
        // Define dependency versions as constraints
        implementation("org.apache.commons:commons-text:1.11.0")
    }
    implementation("org.apache.commons:commons-text")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.10.2")
        }
    }
}
