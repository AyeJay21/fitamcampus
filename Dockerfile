FROM gradle:8.5.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon
FROM eclipse-temurin:17-jre
EXPOSE 9024
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app/spring-boot-application.jar"]