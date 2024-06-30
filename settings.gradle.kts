/*
 * For more detailed information on multi-project builds, please refer to https://docs.gradle.org/8.8/userguide/multi_project_builds.html in the Gradle documentation.
 * This project uses @Incubating APIs which are subject to change.
 */

pluginManagement {
    // Include 'plugins build' to define convention plugins.
    includeBuild("build-logic")
    plugins {
        id("org.siouan.frontend-jdk21") version "8.1.0"
        id("org.jetbrains.kotlin.jvm") version "2.0.0"
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "fullstackkotlinnextjs"
include("backend")
