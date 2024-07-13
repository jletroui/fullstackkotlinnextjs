# The 'node' executable downloaded by the org.siouan.frontend Gradle plugin is built against a non-alpine glibc.
# Although we want alpine for production, we need a non-alpine distro for build.
# eclipse-temurin is based on Ubuntu.
FROM gradle:8.8-jdk21 AS BUILD

WORKDIR /app
ENV NODE_ENV=production

# Builds our app
COPY ./*.kts gradle.properties /app/
COPY ./backend /app/backend
COPY ./ops/dev/setup /app/ops/dev/
RUN gradle :backend:build -x :backend:check && \
    tar --extract --file=/app/backend/build/distributions/backend.tar
COPY ./frontend /app/frontend/
RUN rm /app/frontend/jest.config.ts
RUN gradle :frontend:build -x :frontend:check

FROM amazoncorretto:21-alpine
 # amazoncorretto seems the current go-to performant production JRE of the day (as of 2024)

WORKDIR /app
# Need to provide ENV=production or ENV=staging to execute
ENV APP_HOME=/app

COPY --from=BUILD /app/backend/lib /app/lib
COPY --from=BUILD /app/backend/bin /app/bin
COPY --from=BUILD /app/ops/tools/ejson /app/bin/
COPY --from=BUILD /app/frontend/out /app/www
# Little note: getting the config file from BUILD ensures that the secrets are encrypted, because we ran the build
COPY --from=BUILD /app/backend/config/*.ejson /app/config/
COPY ./ops/docker-entry-point.sh /app/

RUN ls -la && env

ENTRYPOINT ["/app/docker-entry-point.sh"]
