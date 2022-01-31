FROM openjdk:11.0.7-jdk
COPY build/libs/*.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]