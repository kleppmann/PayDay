FROM openjdk:11.0.7-jdk
ARG JAR_FILE=build/libs/service-discovery.jar
COPY ${JAR_FILE} service-discovery.jar
ENTRYPOINT ["java", "-jar", "service-discovery.jar"]