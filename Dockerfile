FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17.0.2-jdk-slim-bullseye

WORKDIR /app

COPY --from=build /app/target/matdata-v1.jar .

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "matdata-v1.jar"]