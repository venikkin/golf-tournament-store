FROM openjdk:11-oraclelinux7

COPY conf/example.application.properties /opt/app/application.properties
COPY target/golf-tournaments-store-1.0-SNAPSHOT.jar /opt/app/golf-tournaments-store.jar

CMD java -jar /opt/app/golf-tournaments-store.jar --spring.config.location=file:/opt/app/application.properties