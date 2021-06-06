# Build stage
FROM maven:3.8.1-jdk-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml protobuf:compile protobuf:compile-custom package -DskipTests

# Package stage
FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/chatmicroservice-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app.jar"]