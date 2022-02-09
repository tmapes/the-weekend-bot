FROM docker.mapes.info/eclipse-temurin:11.0.13_8-jre-alpine

ENV TZ="America/Chicago"

COPY build/layers/libs /app/libs
COPY build/layers/resources /app/resources
COPY build/layers/application.jar /app/application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
