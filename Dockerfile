# Etapa 1: Construcción de la aplicación
FROM eclipse-temurin:17-jdk AS builder

# Define el directorio de trabajo
WORKDIR /app

# Copia el archivo pom.xml para que Maven descargue las dependencias antes de copiar todo el código
COPY pom.xml .

# Copia el resto del código fuente
COPY . .

# Construye la aplicación (sin ejecutar los tests)
RUN ./mvnw clean package -DskipTests

# Etapa 2: Construcción de la imagen final para ejecutar la aplicación
FROM eclipse-temurin:17-jre

# Define el directorio de trabajo
WORKDIR /app

# Copia el archivo .jar generado desde la etapa de construcción
COPY --from=builder /app/target/SAF-0.0.1.jar /app/SafeAlert.jar

# Expone el puerto de la aplicación
EXPOSE 8083

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "SafeAlert.jar"]
