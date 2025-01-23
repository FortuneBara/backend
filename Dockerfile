FROM openjdk:17

WORKDIR /app

ARG VERSION=0.0.1
ENV VERSION=${VERSION}

COPY build/libs/fortune-app-backend-${VERSION}.jar fortune-app.jar

CMD ["java", "-jar", "fortune-app.jar"]
