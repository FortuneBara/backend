FROM openjdk:17

WORKDIR /app

COPY build/libs/fortune-app-backend-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
