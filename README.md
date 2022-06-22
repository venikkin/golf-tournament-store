# Golf Tournament Store

This is an example application intended to accept data about golf tournaments and store them in relational storage.
More detailed description could be found [here](DESCRIPTION.md).

## How to build

This application requires:
* Java 11+
* Maven
* Docker

In order to build application run `mvn clean install`. This will compile the code, run unit and integration tests and package application as a fatjar.
The build setup assumes Linux-compatible environment is used. If Windows is used, few modification related to docker networks and paths are needed to be applied.

In order to run application with Spring Boot plugin assuming MySQL server is provided, you can use `mvn spring-boot:run`.
This command assumes configuration for the spring boot application will be taken from [example file](conf/example.application.properties).
Feel free to adjust it with MySQL connection details.

Alternatively, you can run the application utilising docker by `./example-local-runner.sh`.
This command will build docker images for the application and mysql server with compatible schema, and starts both containers.
Please note, after script is stopped, mysql container will be still running, and you will be responsible to stop and dispose it afterwards
by executing `docker container stop dev-mysql && docker container rm dev-mysql`.

The application is not production-ready because of time limitations.
Relevant sections contain commentaries about possible improvements that could be taken further.

In order to add data provider you need to:
* add provider-specific [payload converter](src/main/kotlin/com/venikkin/example/golftmts/provider/ProviderPayloadConverter.kt);
* extend [configuration](conf/providers.example.json) with provider-specific token and converter qualifier.

