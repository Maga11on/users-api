# Imagen con Maven y JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .

# Compila el proyecto
RUN mvn clean package -DskipTests

# Imagen final con JRE
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/users-api-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]