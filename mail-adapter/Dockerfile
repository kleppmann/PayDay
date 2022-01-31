FROM openjdk:11.0.7-jdk
COPY build/libs/*.jar mail-adapter.jar
ENTRYPOINT ["java", "-jar", "mail-adapter.jar"]