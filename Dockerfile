FROM openjdk:17-jdk-alpine
ADD target/lotto-spring-0.0.1-SNAPSHOT.jar .
#EXPOSE 8080
CMD java -jar lotto-spring-0.0.1-SNAPSHOT.jar