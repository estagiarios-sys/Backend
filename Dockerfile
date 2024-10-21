FROM openjdk:21-jdk-slim

ARG JAR_FILE=relatorio/target/*.jar

COPY ${JAR_FILE} RelatorioApplication.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/RelatorioApplication.jar"]
