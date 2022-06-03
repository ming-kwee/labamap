FROM docker.io/library/adoptopenjdk:11-jre-hotspot
EXPOSE 8080
COPY maven /maven/
ENTRYPOINT ["java","-jar","/maven/cliniva-auth-1.8-SNAPSHOT.jar"]
