FROM maven:3-amazoncorretto-17-alpine AS build
WORKDIR /app
COPY --chown=maven:maven . /app
COPY settings.xml /root/.m2/settings.xml
RUN mvn clean install -Dspring.profiles.active=local -DskipTests

FROM amazoncorretto:17 AS builder
WORKDIR /app
COPY --from=build /app/berkut-service-src/target/berkut-service-src-0.0.1-SNAPSHOT.jar /app/berkut-service-0.0.1-SNAPSHOT.jar
RUN java -Djarmode=layertools -jar berkut-service-0.0.1-SNAPSHOT.jar extract

FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]