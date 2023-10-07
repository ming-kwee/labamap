FROM java:17
FROM maven:alpine

# image layer
WORKDIR /app
ADD pom.xml /app
RUN mvn verify clean --fail-never

# Image layer: with the application
COPY . /app
RUN mvn -v
RUN mvn clean install -DskipTests
EXPOSE 9000
ADD ./target/cliniva-auth-1.0-SNAPSHOT.jar /developments/
ENTRYPOINT ["java","-jar","/developments/cliniva-auth-1.0-SNAPSHOT.jar"]