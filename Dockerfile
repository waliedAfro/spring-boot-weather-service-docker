# ---------- Stage 1: Build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy POM and download dependencies using BuildKit cache
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

# Copy sources and build the jar
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -q clean package -DskipTests

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create and switch to non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar with proper ownership
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# Container-friendly JVM options
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

EXPOSE 9090

# Pass SIGTERM directly to the JVM
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
