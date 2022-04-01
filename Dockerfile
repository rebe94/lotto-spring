FROM openjdk:17-jdk-alpine
ADD target/lotto-spring-0.0.1-SNAPSHOT.jar .
CMD java -jar lotto-spring-0.0.1-SNAPSHOT.jar