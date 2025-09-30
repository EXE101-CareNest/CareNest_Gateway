# Multi-stage build for CareNest Gateway
# 1) Build stage: use JDK with Gradle to produce the fat jar
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /workspace

# Install bash (some Gradle plugins/scripts may expect it) and git (optional for dependencies)
RUN apk add --no-cache bash

# Copy only files required to resolve dependencies first (leveraging Docker layer caching)
COPY settings.gradle .
COPY build.gradle .
COPY gradlew .
COPY gradle gradle

# Prepare wrapper
RUN chmod +x gradlew

# Download dependencies (will be cached unless build files change)
RUN ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

# Now copy the source code
COPY src src

# Build the application jar (skip tests to speed up container build)
RUN ./gradlew --no-daemon clean bootJar -x test

# 2) Runtime stage: small JRE image
FROM eclipse-temurin:17-jre-alpine AS runtime

# Create non-root user
RUN addgroup -S app && adduser -S app -G app
USER app

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

# Configure runtime
EXPOSE 8099
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=default

# Healthcheck (optional but useful)
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s CMD wget -qO- http://localhost:8099/actuator/health | grep '"status":"UP"' || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
