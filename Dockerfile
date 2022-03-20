FROM docker.mapes.info/eclipse-temurin:11.0.13_8-jre-alpine

ENV TZ="America/Chicago"

COPY build/docker/main/layers/libs /app/libs
COPY build/docker/main/layers/resources /app/resources
COPY build/docker/main/layers/application.jar /app/application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
