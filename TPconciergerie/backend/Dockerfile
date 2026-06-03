# ---------------------------------------------------------------
# Build stage — compile le JAR avec Maven
# ---------------------------------------------------------------
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN ./mvnw -q -DskipTests package || \
    (apk add --no-cache maven && mvn -q -DskipTests package)

# ---------------------------------------------------------------
# Run stage — image légère avec uniquement le JRE
# ---------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
