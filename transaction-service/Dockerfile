FROM openjdk:11.0.7-jdk
COPY build/libs/*.jar transaction-service.jar
ENTRYPOINT ["java", "-jar", "transaction-service.jar"]
