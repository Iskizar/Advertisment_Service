FROM openjdk:21

WORKDIR /app

COPY target/final_project.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080
