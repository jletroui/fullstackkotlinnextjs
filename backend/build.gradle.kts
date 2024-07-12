plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.+"
    id("org.flywaydb.flyway") version "10.+"
    // https://github.com/jlleitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "12+"
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
    // Db migrations
    implementation("org.flywaydb:flyway-database-postgresql:10.+")
    runtimeOnly("org.postgresql:postgresql:42.+")
    // Config deserialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.+")
    // HTTP API
    implementation("io.vertx:vertx-web:4.+")
    implementation("io.vertx:vertx-pg-client:4.+")
    implementation("io.vertx:vertx-lang-kotlin:4.+")
    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.+")
    implementation("net.logstash.logback:logstash-logback-encoder:7.+")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.+")
    testImplementation("io.vertx:vertx-junit5:4.+")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.+")
    testImplementation("io.vertx:vertx-web-client:4.+")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    systemProperty("junit.jupiter.extensions.autodetection.enabled", "true")
    useJUnitPlatform()
}

flyway {
    // The database url used when calling the flyway tasks to test migration scripts
    url = "jdbc:postgresql://localhost/backend_test?user=postgres&password=secret&ssl=false"
}

tasks.register<Copy>("installLocalGitHook") {
    // https://dev.to/akdevcraft/git-pre-hook-pre-commit-with-gradle-task-134m
    from(layout.projectDirectory.file("../ops/git/hooks/pre-commit"))
    into(layout.projectDirectory.dir("../.git/hooks"))
    filePermissions {
        user {
            read = true
            execute = true
        }
        other.execute = false
    }
    dependsOn(rootProject.tasks.named("setupDev")) // For installing ejson
}

tasks.compileKotlin {
    // Decrease the risk of leaking secrets on commit
    dependsOn("installLocalGitHook")
}

task<Exec>("encryptConfig") {
    // https://github.com/Shopify/ejson
    group = "Operations"
    description = "Encrypts secrets."
    inputs.dir(layout.projectDirectory.dir("config").files("backend.production.ejson"))
    inputs.dir(layout.projectDirectory.dir("config").files("backend.production.ejson"))
    commandLine("../ops/tools/ejson", "encrypt", "config/backend.production.ejson")
    dependsOn(rootProject.tasks.named("setupDev")) // For installing ejson
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    verbose = true
}
