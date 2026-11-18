FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /workspace
COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

FROM amazoncorretto:17

WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
