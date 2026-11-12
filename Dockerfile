FROM amazoncorretto:17 AS build

WORKDIR /workspace
COPY . .

RUN yum install -y tar gzip && \
    chmod +x mvnw && \
    ./mvnw -DskipTests package

FROM amazoncorretto:17

WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
