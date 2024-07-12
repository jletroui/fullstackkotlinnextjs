import java.io.IOException

tasks.register<Exec>("dbStart") {
    group = "Database"
    description ="Starts the backend database."
    commandLine("docker-compose", "-f", "ops/docker-compose.dev.yml", "up", "--detach", "--no-recreate", "--remove-orphans")
}

tasks.register<Exec>("dbStop") {
    group = "Database"
    description ="Stops the backend database."
    commandLine("docker", "stop", "postgres_ctnr")
}

tasks.register<Exec>("dbReset") {
    group = "Database"
    description ="Resets the backend database data. WARNING! You will permanently lose your data."
    commandLine("docker", "rm", "--force", "postgres_ctnr")
}

tasks.register("checkForPowershell") {
    doFirst {
        if (System.getProperty("os.name").startsWith("Windows")) {
            val pwshFailed = try {
                Runtime.getRuntime().exec(arrayOf("pwsh.exe", "--version")).waitFor() != 0
            } catch(ioe: IOException) {
                true
            }
            if (pwshFailed) {
                throw GradleException("You need to install powershell 7+ from the Microsoft Store")
            }
        }
    }
}

tasks.register("checkForWindowsTerminal") {
    dependsOn("checkForPowershell")
    doFirst {
        if (System.getProperty("os.name").startsWith("Windows")) {
            val wtFailed = try {
                Runtime.getRuntime().exec(arrayOf("pwsh", "--Command", "Get-Command wt.exe")).waitFor() != 0
            } catch(ioe: IOException) {
                true
            }
            if (wtFailed) {
                throw GradleException("You need to install Windows Terminal from the Microsoft Store")
            }
        }
    }
}

tasks.register<Exec>("dbConsoleWindows") {
    onlyIf {
        System.getProperty("os.name").startsWith("Windows")
    }
    commandLine("wt", "--window", "0" ,"-p" , "\"Windows Powershell\"", "pwsh", "-Interactive", "-Command", "\"docker exec -it postgres_ctnr psql -U postgres\"")
    dependsOn("checkForWindowsTerminal")
}

tasks.register<Exec>("dbConsoleUnix") {
    onlyIf {
        !System.getProperty("os.name").startsWith("Windows")
    }
    commandLine("echo", "DB Console on Unix is not yet implemented. Work on it in build.gradle.kts #dbConsoleUnix")
}

tasks.register("dbConsole") {
    group = "Database"
    description ="Starts the psql console in a new terminal."
    dependsOn("dbConsoleUnix", "dbConsoleWindows")
}

tasks.register<Exec>("setupDevWindows") {
    onlyIf {
        System.getProperty("os.name").startsWith("Windows")
    }
    dependsOn("checkForPowershell")
    commandLine("pwsh", "-NoProfile", "-File", "\"ops\\dev\\setup.ps1\"")
    outputs.file(layout.projectDirectory.dir("ops").dir("tools").file("ejson.exe"))
}

tasks.register<Exec>("setupDevUnix") {
    onlyIf {
        !System.getProperty("os.name").startsWith("Windows")
    }
    commandLine("bash", layout.projectDirectory.dir("ops").dir("dev").file("setup").toString())
    outputs.file(layout.projectDirectory.dir("ops").dir("tools").file("ejson"))
}

tasks.register("setupDev") {
    group = "Operations"
    description ="Setup the dev environment"
    dependsOn("setupDevWindows", "setupDevUnix")
}
