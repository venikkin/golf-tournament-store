FROM openjdk:11-oraclelinux7

COPY ./conf/example.application.properties /opt/app/application.properties
COPY ./conf/providers.example.json /opt/app/providers.json
COPY ./target/golf-tournaments-store-1.0-SNAPSHOT-app.jar /opt/app/golf-tournaments-store.jar

EXPOSE 8080

CMD java -jar /opt/app/golf-tournaments-store.jar \
    --spring.config.location=file:/opt/app/application.properties \
    --providers.source=file:/opt/app/providers.json