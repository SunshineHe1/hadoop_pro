# Build
FROM maven:3.8.7-eclipse-temurin-8-focal
ARG SKIP_TESTS=true

WORKDIR /app

COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn -ntp clean install -DskipTests=$SKIP_TESTS

