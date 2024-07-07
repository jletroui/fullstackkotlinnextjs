import java.io.IOException

task<Exec>("dbStart") {
    group = "Database"
    description ="Starts the backend database."
    commandLine("docker-compose", "-f", "ops/docker-compose.dev.yml", "up", "--detach", "--no-recreate")
}

task<Exec>("dbStop") {
    group = "Database"
    description ="Stops the backend database."
    commandLine("docker", "stop", "postgres_ctnr")
}

task<Exec>("dbReset") {
    group = "Database"
    description ="Resets the backend database data. WARNING! You will permanently lose your data."
    commandLine("docker", "rm", "--force", "postgres_ctnr")
}

tasks.register("checkForWindowsTerminal") {
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

task<Exec>("dbConsoleWindows") {
    onlyIf {
        System.getProperty("os.name").startsWith("Windows")
    }
    commandLine("wt", "--window", "0" ,"-p" , "\"Windows Powershell\"", "pwsh", "-Interactive", "-Command", "\"docker exec -it postgres_ctnr psql -U postgres\"")
    dependsOn("checkForWindowsTerminal")
}

task<Exec>("dbConsoleUnix") {
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
