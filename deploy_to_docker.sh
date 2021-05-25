#!/bin/bash

# build and bring package to docker folder
cd thirdparties-voucher-service
./mvnw clean package
cp target/thirdparties-voucher-service-0.0.1-SNAPSHOT.jar src/main/docker/
cd -

# build and bring package to docker folder
cd voucher-service
./mvnw clean package
cp target/voucher-service-0.0.1-SNAPSHOT.jar src/main/docker/
cd -

docker-compose up
