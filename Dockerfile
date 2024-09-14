# Étape 1: Utilise une image officielle Gradle avec JDK 21 pour la construction
FROM gradle:8.3-jdk21 AS build

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie uniquement les fichiers de configuration Gradle pour télécharger les dépendances en premier
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

# Télécharge les dépendances
RUN gradle build --no-daemon || return 0

# Copie le reste des fichiers du projet
COPY . .

# Compile et génère l'artefact JAR pour l'application
RUN gradle bootJar --no-daemon

# Étape 2: Utilise une image JDK légère pour exécuter l'application
FROM eclipse-temurin:21-jdk-alpine

# Crée un répertoire pour l'application
WORKDIR /app

# Copie le fichier JAR généré depuis l'étape de construction
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose le port 8080 pour l'application Spring Boot
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]