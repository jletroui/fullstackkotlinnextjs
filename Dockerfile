# The 'node' executable downloaded by the org.siouan.frontend Gradle plugin is built against a non-alpine glibc.
# Although we want alpine for production, we need a non-alpine distro for build.
# eclipse-temurin is based on Ubuntu.
FROM eclipse-temurin:21-jdk AS BUILD

WORKDIR /app

# Installs Gradle and preserve the result in Docker's cache
# Any call to gradlew will download the right version of gradle, so just execute 'tasks'
COPY ./gradle ./gradle
COPY ./gradlew .
RUN touch build.gradle && /app/gradlew tasks && rm build.gradle

# Builds our app
COPY . .
ENV NODE_ENV=production
RUN /app/gradlew :backend:build -x :backend:check && \
    tar --extract --file=/app/backend/build/distributions/backend.tar
RUN rm /app/frontend/jest.config.ts
RUN /app/gradlew :frontend:build -x :frontend:check

FROM amazoncorretto:21-alpine

WORKDIR /app
# Need to provide ENV=production or ENV=staging to execute
ENV APP_HOME=/app

COPY --from=BUILD /app/backend/lib /app/lib
COPY --from=BUILD /app/backend/bin /app/bin
COPY --from=BUILD /app/backend/secrets/*.ejson /app/secrets/
COPY --from=BUILD /app/frontend/out /app/www

RUN ls -la && env

EXPOSE 8080/tcp
