# Uso la imagen de Maven que ya tiene Java 23
FROM maven:3.9-eclipse-temurin-23

# Creo la carpeta de trabajo
WORKDIR /app

# Copio todo el código del proyecto
COPY . .

# Compilo el proyecto y creo el .jar
# El -DskipTests es para que vaya más rápido y no falle si hay tests tontos
RUN mvn clean package -DskipTests

# Expongo el puerto 8080 del Tomcat
EXPOSE 8080

# Ejecuto el jar que se ha creado en la carpeta target
CMD ["java", "-jar", "target/EternalsGardens-0.0.1-SNAPSHOT.jar"]
