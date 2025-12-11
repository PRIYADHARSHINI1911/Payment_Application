# ---- Build stage ----
FROM gradle:8.5-jdk17 AS builder
WORKDIR /home/gradle/project

COPY . .
RUN chmod +x ./gradlew || true
RUN ./gradlew bootJar -x test

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the JAR built in the first stage
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
