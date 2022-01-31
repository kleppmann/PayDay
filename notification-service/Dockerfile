FROM openjdk:11.0.7-jdk
COPY build/libs/*.jar notification-service.jar
ENTRYPOINT ["java", "-jar", "notification-service.jar"]