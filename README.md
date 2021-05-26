# Voucher

## 1. System requirements
- Docker
- Docker compose
- Java 8 or later
- Free ports: 8081, 8082, 8432, 9081

## 2. Build and run application
#### Keycloak and Postgres on docker containers
Ports will be used: 8091 (Keycloak), 8432 (Postgres)

Goto folder vdc_voucher_assignment:
`docker-compose up`
#### Run services in local
Ports will be used: 8081 (voucher-service), 8082 (thirdparties-voucher-service)

Goto folder voucher-service: `cd ./voucher-service`

Run build: `./mvnw clean package`

Run voucher-service service: `java -jar ./target/voucher-service-0.0.1-SNAPSHOT.jar`

Goto folder thirdparties-voucher-service: `cd ./thirdparties-voucher-service`

Run build: `./mvnw clean package`

Run thirdparties-voucher-service service: `java -jar ./target/thirdparties-voucher-service-0.0.1-SNAPSHOT.jar`

##3. Test


##4. Coverage test result
To see coverage test results, go to **<service_name>/target/site/jacoco/index.html**