FROM docker.mapes.info/tjmapes/jre-dumb-init:1.0.0@sha256:26144720d35253370fb6a2aace1202fa79b0e5bcac232d5a3b91984fd4e012bc

COPY build/docker/main/layers/libs /app/libs
COPY build/docker/main/layers/resources /app/resources
COPY build/docker/main/layers/app/application.jar /app/application.jar

CMD ["java", "-jar", "/app/application.jar"]
