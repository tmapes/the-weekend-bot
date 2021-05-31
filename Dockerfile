FROM docker.mapes.info/adoptopenjdk/openjdk11:alpine-jre

ENV TZ="America/Chicago"

COPY build/layers/libs /app/libs
COPY build/layers/resources /app/resources
COPY build/layers/application.jar /app/application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
