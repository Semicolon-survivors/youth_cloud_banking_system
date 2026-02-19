# ================================
# BUILD STAGE
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

RUN apt-get update && apt-get install -y ca-certificates && update-ca-certificates

# Create Maven settings.xml to force HTTP mirror
RUN mkdir -p /root/.m2 && \
    printf '%s\n' \
    '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"' \
    '          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"' \
    '          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">' \
    '  <mirrors>' \
    '    <mirror>' \
    '      <id>google-maven-central</id>' \
    '      <mirrorOf>central</mirrorOf>' \
    '      <url>https://maven-central.storage-download.googleapis.com/maven2/</url>' \
    '    </mirror>' \
    '  </mirrors>' \
    '</settings>' \
    > /root/.m2/settings.xml


COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# ================================
# RUNTIME STAGE
# ================================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
