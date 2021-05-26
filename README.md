# Voucher

## 1. System requirements
- Docker
- Docker compose
- Java 8 or later
- Free ports: 8081, 8082, 8432, 9081

## 2. Build and run application
#### Keycloak and Postgres on docker containers
Ports will be used: 9081 (Keycloak), 8432 (Postgres)

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

## 3. Demo
Import postman collection first to have request samples: open Postman app and import collection: ![Exercises.postman_collection.json](Exercises.postman_collection.json)

### Get vouchers by phone number
Get access token first
Then use access token to call request to get vouchers by phone number. (Authorization: Bearer <access_token>)
Note: this application designed that only **User** role has permission to get vouchers

Example:
User role: can access resource
![](images/get_access_token_for_user.PNG) ![](images/then_get_voucher_by_user.PNG)
Admin role: will receive a forbidden message
![](images/get_access_token_for_admin.PNG) ![](images/then_access_denied_for_admin.PNG)
Person who has both User and Admin role:
![](images/get_access_token_for_person_both_role_admin_user.PNG) ![](images/then_get_voucher_by_both_user_or_admin_.PNG)

### Buy a voucher
Return a voucher code. But when process time is larger than 30s then user will receive a message response that long process. 
And you will be received a SMS text message .

**Limited: SMS text message is not implemented yet**

Example:

Return voucher code when process time is short.
![](images/buy_voucher_in_1s.PNG)

Return voucher code when process time is longer than 30s.
Note that if process is running, when you get bought vouchers for this number. This buying voucher will not appear in result.
This buying voucher will appear when process running finish. (Use getVoucher request to check when processing and when processed) 
![](images/buy_voucher_in_60s_process_time.PNG)

## 4. Coverage test result
To see coverage test results, go to **<service_name>/target/site/jacoco/index.html**

![](images/test_cover_voucher_service.PNG)
![](images/test_cover_thirdparties-voucher_service.PNG)
