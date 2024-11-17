FROM gradle:8.10-jdk-21-and-22 AS build

COPY . /app

WORKDIR /app

RUN gradle test --no-daemon && gradle build --no-daemon
#RUN gradle init
#ENTRYPOINT ./gradlew bootRun

FROM eclipse-temurin:21-jre-jammy AS final

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]