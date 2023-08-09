FROM docker.mapes.info/eclipse-temurin:11.0.13_8-jre-alpine

ENV TZ="UTC"

RUN curl -sLo /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.5/dumb-init_1.2.5_x86_64 \
    && chmod +x /usr/local/bin/dumb-init

COPY build/docker/main/layers/libs /app/libs
COPY build/docker/main/layers/resources /app/resources
COPY build/docker/main/layers/application.jar /app/application.jar

ENTRYPOINT [ "/usr/local/bin/dumb-init", "--" ]
CMD ["java", "-jar", "/app/application.jar"]
