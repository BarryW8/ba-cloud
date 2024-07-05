FROM openjdk:8-jdk-alpine
LABEL authors="barryw"
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 7777
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod"]