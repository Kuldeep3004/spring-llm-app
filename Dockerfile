# ─────────────────────────────────────────────
# Stage 1: Build (Maven)
# ─────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build the app
COPY src ./src
RUN mvn clean package -DskipTests --batch-mode

# ─────────────────────────────────────────────
# Stage 2: Runtime (Lightweight JRE)
# ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Security: non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Model directory (2.8GB model yahan mount hoga via volume)
# IMPORTANT: Model ko image me mat daalo - bahut bada hoga!
# Use: docker run -v /host/model/path:/app/models ...
RUN mkdir -p /app/models && chown -R appuser:appuser /app

USER appuser

# Spring Boot default port
EXPOSE 8080

# JVM tuning for containers
ENV JAVA_OPTS="-Xms512m -Xmx1024m \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseG1GC \
  -Djava.security.egd=file:/dev/./urandom"

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
