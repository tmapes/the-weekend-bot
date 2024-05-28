FROM docker.mapes.info/eclipse-temurin:17.0.11_9-jre-alpine@sha256:ad9223070abcf5716e98296a98c371368810deb36197b75f3a7b74815185c5e3

ENV TZ="UTC"

RUN wget -qO /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.5/dumb-init_1.2.5_x86_64 \
    && chmod +x /usr/local/bin/dumb-init

COPY build/docker/main/layers/libs /app/libs
COPY build/docker/main/layers/resources /app/resources
COPY build/docker/main/layers/app/application.jar /app/application.jar

ENTRYPOINT [ "/usr/local/bin/dumb-init", "--" ]
CMD ["java", "-jar", "/app/application.jar"]
