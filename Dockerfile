FROM openjdk:8-alpine
COPY javaparser-maven-sample-1.0-SNAPSHOT-shaded.jar javaparser-maven-sample-1.0-SNAPSHOT-shaded.jar
EXPOSE 8080
CMD ["java", "-jar", "javaparser-maven-sample-1.0-SNAPSHOT-shaded.jar"]
