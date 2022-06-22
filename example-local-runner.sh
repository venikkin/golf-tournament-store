#!/usr/bin/env sh

# This scripts is an example. It doesn't require and 3party tools (docker-compose, mysql-client) to be installed. It does leave a dirty state
# in a form of mysql container running after termination. Please stop / remove it separately.

docker build -t golf-tournamnets .
docker build -t dev-mysql database

MYSQL_CONTAINER_NAME=dev-mysql
if [ "$(docker container ls -a | grep -c $MYSQL_CONTAINER_NAME)" -gt 0 ];
then
    docker container stop $MYSQL_CONTAINER_NAME
    docker container rm $MYSQL_CONTAINER_NAME
fi
docker run -it --name $MYSQL_CONTAINER_NAME \
    -e MYSQL_USER=dev \
    -e MYSQL_PASSWORD=dev-pwd \
    -e MYSQL_DATABASE=golf \
    -e MYSQL_ALLOW_EMPTY_PASSWORD=true \
    -p 127.0.0.1:3306:3306 \
    -d dev-mysql:latest

sleep 30
docker run --network host golf-tournamnets:latest
