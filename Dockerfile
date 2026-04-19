# === Build Stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# === Runtime Stage ===
# Note: Using JDK instead of JRE for JSP compilation support
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the war from the build stage using a wildcard
COPY --from=build /app/target/*.war app.war

# Run the app (Yes, java -jar works perfectly on Spring Boot WARs!)
ENTRYPOINT ["java", "-jar", "app.war"]