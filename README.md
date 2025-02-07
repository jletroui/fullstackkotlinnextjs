## Overview

Application template supporting the common dev and operations for the combination Kotlin (backend) and NextJS (frontend).
The goal was to explore how would look a modern full stack app with speed of maintainability over long period of time as its main driver. 

Supports:
- cross-platform dev operations command line (Gradle)
- dependencies management (Gradle and npm)
- dev database, database migrations (Postgres and Flyway)
- performant web server (Vert.x)
- configuration and secrets
- backend and frontend linting
- backend and frontend unit tests for the example data access layer, controller, and page
- structured logging
- deployment (production simulated locally)
- frontend and backend CI

Not covered:
- monitoring (other than logging)
- authentication
- SSL termination

## Reasoning behind the tech choices

### Backend and frontend languages

You start a project once, and you maintain it again and again. Thus, maintainability has been, for me, the main driver of team success.
By maintainable, I take the definition of 'all modifications of comparable complexity do not consume more dev time over the lifetime of the project'.
Therefore, it needs to be easy to navigate and understands the code. This favors statically typed languages, for which IDE have a lot better navigability, and compiler are helping a lots more.
Ideally, it is also quick to develop. Which means, a language that is preventing technical bugs, like memory issues or null values.
On this scale alone, languages like Haskell or Rust are clearly well-placed. Except, to be maintainable, you also need to find developers.
Most used languages today are Python and Javascript, but they are too far on the 'expensive to maintain' scale.
Static languages Go and Java attracts a fair amount of developer. But Java is not preventing a lot of bugs. Kotlin however is close enough and has excellent support for nullability prevention and immutability.
This repository is exploring this alternative.

On the frontend, if we exclude the webassembly languages, the choice lies between Javascript and Typescript. The most maintainable is Typescript.

### Build tool

It seems the most mature and flexible is Gradle on the JVM. It also has support for build files in the chosen language, which is a plus.

### Database

Staying in the SQL side of things, Postgres is slightly simpler and more reliable than Mysql, although both would be ok.
We want things to be simple to understand and debug (maintainability), so no ORM.

### Backend stack

There are so many performant stacks today. It seems on the JVM, Vert.x is one of the more performant, excellent documentation, vibrant community. Also, a lot simpler than other stacks like Akka.HTTP, the Spring eco system or Java Enterprise.

### Secrets

This template assumes the project does not have access to a vault.

Solutions exposing secrets in files or env vars in production containers are less secured. So a solution exposing public encryption keys within the git repository, and piping the decrypted secrets to the application seems the next best choice after a Vault.
The 2 alternatives explored, because they are not tied to any particular language and are easy to install both in dev and in whatever linux distro are [eJson](https://github.com/Shopify/ejson) and [SOPS/Age](https://github.com/getsops/sops?tab=readme-ov-file#encrypting-using-age).

Using SOPS would have been the better choice with a vault, but in the absence of one, ejson is simpler to use.

## Prerequisite

### On Windows

- From the Microsoft Store:
  - [Windows Terminal](https://www.microsoft.com/store/productId/9N0DX20HK701?ocid=pdpshare)
  - [Powershell 7+](https://www.microsoft.com/store/productId/9MZ1SNWT0N5D?ocid=pdpshare)
- [Chocolatey](https://chocolatey.org/install)
- From Chocolatey (in an administrative shell):

    `choco install -y corretto21jdk git docker-desktop age.portable sops`

- *Optional*: from Chocolatey, the best IDE for the project (in an administrative shell):

    `choco install -y intellijidea-community`

### On MacOS

Instructions TODO, but: must install git, Corretto 21 JDK, Docker Desktop (which includes Docker Compose).

### On Linux

Instructions TODO, but must install git, Corretto 21 JDK, Docker Desktop (which includes Docker Compose).

## Common operations

### Backend

Start the Postgres dev database (requires Docker Desktop to be up):

    .\gradlew.bat dbStart

Stop the Postgres dev database:

    .\gradlew.bat dbStop

Open the Postgres console:

    .\gradlew.bat dbConsole

Start the backend:

In your IDE, starts the org.example.app.App main class. 

Test a new DB migration:

    .\gradlew.bat flywayMigrate -i

Run tests:

    .\gradlew.bat :backend:test

Lint:

    .\gradlew.bat ktlintCheck

Run the backend (although you should start it in debug mode from your IDE):

    .\gradlew.bat :backend:run

Encrypt configuration secrets:

    .\gradlew.bat :encryptConfig

### Frontend

Run dev server:

    .\gradlew.bat :frontend:run

Run tests:

    .\gradlew.bat :frontend:test

or (for colored output):

    pushd frontend; npm run test; popd 

Lint:

    .\gradlew.bat :frontend:lint

## Operations

CI:

    ops/run_ci.sh

Deployment:

    ops/simulate_prod_deployment.sh
