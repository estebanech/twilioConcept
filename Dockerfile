FROM openjdk:8-jdk-alpine
LABEL maintainer="echeverri.esteban@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} demo.jar
ENTRYPOINT ["java","-jar","/demo.jar"]
