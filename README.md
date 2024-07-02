## Prerequisite

### On Windows

- From the Microsoft Store:
  - [Windows Terminal](https://www.microsoft.com/store/productId/9N0DX20HK701?ocid=pdpshare)
  - [Powershell 7+](https://www.microsoft.com/store/productId/9MZ1SNWT0N5D?ocid=pdpshare)
- [Chocolatey](https://chocolatey.org/install)
- From Chocolatey (in an administrative shell):

    `choco install -y corretto21jdk git docker-desktop`

- *Optional*: from Chocolatey, the best IDE for the project (in an administrative shell):

    `choco install -y intellijidea-community`

### On MacOS

Instructions TODO, but: must install git, Corretto 21 JDK, Docker Desktop (which includes Docker Compose).

### On Linux

Instructions TODO, but must install git, Corretto 21 JDK, Docker Desktop (which includes Docker Compose).

## Common operations

## Backend

Start the Postgres dev database (requires Docker Desktop to be up):

    .\gradlew.bat dbStart

Stop the Postgres dev database:

    .\gradlew.bat dbStop

Open the Postgres console:

    .\gradlew.bat dbConsole

Test a new DB migration:

    .\gradlew.bat flywayMigrate -i

Run tests:

    .\gradlew.bat :backend:test

Run the backend (although you should start it in debug mode from your IDE):

    .\gradlew.bat :backend:run

## Frontend