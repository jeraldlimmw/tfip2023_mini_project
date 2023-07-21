# PART 1: Creating a container
# create an image based off this image
FROM maven:3.8-openjdk-18-slim AS builder

# create a directory and cd into the directory
WORKDIR /src

# everything after WORKDIR is now in /src
# copy pom.xml as pom.xml (. means no name change)
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .

# copy directories (cannot use ., must give it a directory name)
COPY src src

# compile the Java program
RUN mvn package -Dmaven.test.skip=true

# PART 2: Creating a second container in first container
# run the application from the container
FROM openjdk:18-slim

WORKDIR /src

# copy the compiled JAR file to this new container
COPY --from=builder /src/target/billbackend-0.0.1-SNAPSHOT.jar .

ENV SERVER_PORT=8080
EXPOSE ${SERVER_PORT}

# for multi layer build (w Part 2):
ENTRYPOINT java -jar billbackend-0.0.1-SNAPSHOT.jar