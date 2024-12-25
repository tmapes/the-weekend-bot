FROM tjmapes/jre-dumb-init:1.0.1@sha256:22a846ec7f4e6379b46d5979672278b9a6944e72ec748d6664b54983171a1d2e

COPY build/docker/main/layers/libs /app/libs
COPY build/docker/main/layers/resources /app/resources
COPY build/docker/main/layers/app/application.jar /app/application.jar

CMD ["java", "-jar", "/app/application.jar"]
