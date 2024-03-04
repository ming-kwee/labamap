FROM java:17
FROM maven:alpine



# Install dependencies
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Download ChromeDriver
RUN wget -qO /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/94.0.4606.41/chromedriver_linux64.zip \
    && unzip /tmp/chromedriver.zip -d /usr/local/bin/ \
    && rm /tmp/chromedriver.zip \
    && chmod +x /usr/local/bin/chromedriver

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