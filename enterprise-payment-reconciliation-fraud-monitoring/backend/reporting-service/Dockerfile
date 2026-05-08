FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
RUN mvn -B -DskipTests -pl reporting-service -am package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/reporting-service/target/reporting-service-1.0.0.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/app/app.jar"]
