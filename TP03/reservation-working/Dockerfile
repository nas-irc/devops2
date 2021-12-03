FROM maven:3.8.1-openjdk-11-slim AS builder
WORKDIR /resa
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:11-jre

###############
# Application #
###############

WORKDIR /resa
COPY --from=builder /resa/target/*.jar /resa/app.jar
COPY files/statics ./files/statics

# Set timezone
RUN rm /etc/localtime && ln -s /usr/share/zoneinfo/Europe/Paris /etc/localtime && \
    echo "Europe/Paris" > /etc/timezone

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.9.0/opentelemetry-javaagent.jar opentelemetry-javaagent.jar 
ENTRYPOINT java $JAVA_OPTS -javaagent:opentelemetry-javaagent.jar -jar app.jar