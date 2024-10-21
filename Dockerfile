FROM openjdk:21-jdk-slim

# Etapa para construir o projeto com Maven
WORKDIR /app
COPY relatorio/pom.xml .
COPY relatorio/src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -f ./pom.xml

# Copia o arquivo JAR gerado
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} RelatorioApplication.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/RelatorioApplication.jar"]
