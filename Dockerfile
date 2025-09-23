# ============================
# Stage 1: Build
# ============================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias (cacheable)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el resto del código y compilamos
COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo
WORKDIR /app

# Copiamos el .jar generado desde el stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto (modificalo si tu app usa otro)
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]