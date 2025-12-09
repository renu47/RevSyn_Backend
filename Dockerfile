# Use OpenJDK 11
FROM openjdk:11-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven-built JAR into container
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
