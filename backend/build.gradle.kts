plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.flywaydb.flyway") version "10.+"
    application
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.+")
    }
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
    implementation("org.flywaydb:flyway-database-postgresql:10.+")
    runtimeOnly("org.postgresql:postgresql:42.+")

    testImplementation("org.junit.jupiter:junit-jupiter:5.+")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
    useJUnitPlatform()
}

flyway {
    url = "jdbc:postgresql://localhost/backend_test?user=postgres&password=secret&ssl=false"
}
